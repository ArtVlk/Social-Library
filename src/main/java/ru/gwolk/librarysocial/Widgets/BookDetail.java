package ru.gwolk.librarysocial.Widgets;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gwolk.librarysocial.CRUDRepositories.BookRepository;
import ru.gwolk.librarysocial.CRUDRepositories.UserRepository;
import ru.gwolk.librarysocial.Entities.Book;
import ru.gwolk.librarysocial.Entities.Role;
import ru.gwolk.librarysocial.Entities.User;
import ru.gwolk.librarysocial.Entities.UserBook;
import ru.gwolk.librarysocial.CRUDRepositories.UserBookRepository;
import ru.gwolk.librarysocial.Services.CurrentUserService;

import java.util.Optional;

@SpringComponent
@UIScope
public class BookDetail extends VerticalLayout {
    private final UserBookRepository userBookRepository;
    private final BookRepository bookRepository;
    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;
    private Book currentBook;

    private final TextField authorField = new TextField("Автор и название");
    private final TextArea descriptionField = new TextArea("Описание");
    private final TextField ratingField = new TextField("Оценка");
    private Button saveRatingButton = new Button("Сохранить оценку");
    private final NumberField newRatingField = new NumberField("Поставить оценку");
    private Button addToLibraryButton = new Button("Добавить в библиотеку");
    private final Button editBookButton = new Button("Редактировать");

    @Autowired
    public BookDetail(BookRepository bookRepository, UserBookRepository userBookRepository,
                      CurrentUserService currentUserService, UserRepository userRepository) {
        this.userBookRepository = userBookRepository;
        this.bookRepository = bookRepository;
        this.currentUserService = currentUserService;
        this.userRepository = userRepository;


        setHorizontalComponentAlignment(Alignment.CENTER, authorField, descriptionField, addToLibraryButton, ratingField,
                newRatingField, saveRatingButton, editBookButton);
        add(authorField, descriptionField, addToLibraryButton, ratingField, newRatingField, saveRatingButton, editBookButton);

        authorField.setReadOnly(true);
        descriptionField.setReadOnly(true);
        ratingField.setReadOnly(true);

        editBookButton.addClickListener(e -> editCurrentBook());
        addToLibraryButton.addClickListener(e -> addToLibrary());
        saveRatingButton.addClickListener(e -> saveRating());


        configureEditButtonVisibility();
        setVisible(false);
    }

    private void configureEditButtonVisibility() {
        User currentUser = currentUserService.getCurrentUser();
        if (currentUser != null && currentUser.getRole() == Role.ADMIN) {
            editBookButton.setVisible(true);
        } else {
            editBookButton.setVisible(false);
        }
    }

    private void addToLibrary() {
        if (!validateAddToLibraryInputs()) return;
        User currentUser = currentUserService.getCurrentUser();
        if (isBookAlreadyInLibrary(currentUser)) {
            Notification.show("Эта книга уже в вашей библиотеке.");
        } else {
            addBookToUserLibrary(currentUser);
            Notification.show("Книга добавлена в вашу библиотеку.");
        }
    }


    private boolean validateAddToLibraryInputs() {
        if (currentBook == null) {
            Notification.show("Ошибка: книга не найдена. Пожалуйста, выберите книгу из списка.");
            return false;
        }
        if (currentUserService.getCurrentUser() == null) {
            Notification.show("Ошибка: пользователь не выбран.");
            return false;
        }
        return true;
    }

    private boolean isBookAlreadyInLibrary(User currentUser) {
        return userBookRepository.findByUserAndBook(currentUser, currentBook).isPresent();
    }

    private void addBookToUserLibrary(User currentUser) {
        UserBook userBook = new UserBook();
        userBook.setUser(currentUser);
        userBook.setBook(currentBook);
        userBook.setUserRating(currentBook.getStars());
        currentUser.getBooks().add(currentBook);

        userBookRepository.save(userBook);
        userRepository.save(currentUser);
    }

    public void editBook(Book book) {
        this.currentBook = book;
        if (book == null) {
            setVisible(false);
            return;
        }
        setVisible(true);

        authorField.setValue(book.getAuthor().getName() + ": " + book.getName());
        descriptionField.setValue(book.getDescription() != null ? book.getDescription() : "Описание отсутствует.");
        ratingField.setValue(book.getStars() != null ? String.valueOf(book.getStars()) : "Нет рейтинга");

        configureEditButtonVisibility();
    }

    private void saveRating() {
        if (!validateSaveRatingInputs()) return;
        initializeBookRatingFields();

        int newStars = newRatingField.getValue().intValue();
        User currentUser = currentUserService.getCurrentUser();

        Optional<UserBook> existingRating = userBookRepository.findByUserAndBook(currentUser, currentBook);
        if (existingRating.isPresent()) {
            updateExistingRating(existingRating.get(), newStars);
        } else {
            addNewRating(currentUser, newStars);
        }

        updateBookRating(newStars);
        bookRepository.save(currentBook);

        Notification.show("Оценка сохранена!");
    }

    private void initializeBookRatingFields() {
        if (currentBook.getSumStars() == null) {
            currentBook.setSumStars(currentBook.getStars());
        }
        if (currentBook.getEstimationCount() == null) {
            currentBook.setEstimationCount(1);
        }
    }

    private boolean validateSaveRatingInputs() {
        if (currentBook == null) {
            Notification.show("Ошибка: книга не выбрана");
            return false;
        }

        if (currentUserService.getCurrentUser() == null) {
            Notification.show("Ошибка: пользователь не найден");
            return false;
        }

        if (newRatingField.getValue() == null) {
            Notification.show("Пожалуйста, введите оценку");
            return false;
        }

        int newStars = newRatingField.getValue().intValue();
        if (newStars < 1 || newStars > 5) {
            Notification.show("Оценка должна быть от 1 до 5");
            return false;
        }

        return true;
    }

    private void updateExistingRating(UserBook userBook, int newStars) {
        int oldRating = userBook.getUserRating() != null ? userBook.getUserRating() : 0;
        currentBook.setSumStars(currentBook.getSumStars() - oldRating + newStars);
        userBook.setUserRating(newStars);
        userBookRepository.save(userBook);
    }

    private void addNewRating(User currentUser, int newStars) {
        UserBook userBook = new UserBook();
        userBook.setUser(currentUser);
        userBook.setBook(currentBook);
        userBook.setUserRating(newStars);
        currentBook.setSumStars(currentBook.getSumStars() + newStars);
        currentBook.setEstimationCount(currentBook.getEstimationCount() + 1);
        userBookRepository.save(userBook);
    }

    private void updateBookRating(int newStars) {
        if (currentBook.getEstimationCount() == null || currentBook.getEstimationCount() == 0) {
            currentBook.setEstimationCount(1);
        }

        if (currentBook.getSumStars() == null) {
            currentBook.setSumStars(0);
        }

        if (currentBook.getEstimationCount() > 0) {
            currentBook.setStars(currentBook.getSumStars() / currentBook.getEstimationCount());
        }
    }

    private void editCurrentBook() {
        Notification.show("Редактирование книги...");
    }
}