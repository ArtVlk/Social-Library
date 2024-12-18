package ru.gwolk.librarysocial.AppFrontend.SubPresenters;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.BookRepository;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.UserRepository;
import ru.gwolk.librarysocial.AppBackend.CommonServices.CustomNotification;
import ru.gwolk.librarysocial.AppBackend.Entities.Book;
import ru.gwolk.librarysocial.AppBackend.Entities.User;
import ru.gwolk.librarysocial.AppBackend.Entities.UserBook;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.UserBookRepository;
import ru.gwolk.librarysocial.AppBackend.SocialServices.CurrentUserService;

import java.util.Optional;

/**
 * Презентер для отображения и взаимодействия с деталями книги.
 * Этот класс управляет отображением информации о книге, включая её описание, автора, рейтинг,
 * а также позволяет пользователю добавлять книгу в свою библиотеку и сохранять оценки.
 */
@SpringComponent
@UIScope
public class BookDetailPresenter extends VerticalLayout {
    private final UserBookRepository userBookRepository;
    private final BookRepository bookRepository;
    private final CurrentUserService currentUserService;
    private Book currentBook;

    private final TextField authorField = new TextField("Автор и название");
    private final TextArea descriptionField = new TextArea("Описание");
    private final TextField ratingField = new TextField("Оценка");
    private Button saveRatingButton = new Button("Сохранить оценку");
    private final NumberField newRatingField = new NumberField("Поставить оценку");
    private Button addToLibraryButton = new Button("Добавить в библиотеку");
    private Button cancelButton = new Button("Отмена");

    /**
     * Конструктор для инициализации всех необходимых зависимостей.
     *
     * @param bookRepository репозиторий для работы с книгами.
     * @param userBookRepository репозиторий для работы с личными книгами пользователя.
     * @param currentUserService сервис для получения текущего пользователя.
     * @param userRepository репозиторий для работы с пользователями.
     */
    @Autowired
    public BookDetailPresenter(BookRepository bookRepository, UserBookRepository userBookRepository,
                               CurrentUserService currentUserService, UserRepository userRepository) {
        this.userBookRepository = userBookRepository;
        this.bookRepository = bookRepository;
        this.currentUserService = currentUserService;

        descriptionField.setWidth("80%");
        descriptionField.setHeight("200px");

        authorField.setWidth("80%");

        HorizontalLayout actionsLayout = new HorizontalLayout(saveRatingButton, cancelButton);

        setHorizontalComponentAlignment(Alignment.CENTER, authorField, descriptionField, addToLibraryButton, ratingField,
                newRatingField, actionsLayout);
        add(authorField, descriptionField, ratingField, newRatingField, addToLibraryButton, actionsLayout);

        authorField.setReadOnly(true);
        descriptionField.setReadOnly(true);
        ratingField.setReadOnly(true);

        addToLibraryButton.addClickListener(e -> addBookToUserLibrary(currentUserService.getCurrentUser()));
        saveRatingButton.addClickListener(e -> saveRating());
        cancelButton.addClickListener(e -> cancelWatching());

        setVisible(false);
    }

    private void cancelWatching() {
        setVisible(false);
    }

    private void addBookToUserLibrary(User currentUser) {

        if (userBookRepository.findByUserAndBook(currentUser, currentBook).isPresent()) {
            CustomNotification.showNotification("Эта книга уже в вашей библиотеке",
                    NotificationVariant.LUMO_ERROR);
            return;
        }

        UserBook userBook = new UserBook();
        userBook.setUser(currentUser);
        userBook.setBook(currentBook);
        userBook.setUserRating(currentBook.getStars());

        userBookRepository.save(userBook);
        CustomNotification.showNotification("Книга добавлена в вашу библиотеку!",
                NotificationVariant.LUMO_SUCCESS);
    }

    public void editBook(Book book) {
        this.currentBook = book;
        if (book == null) {
            setVisible(false);
            return;
        }
        setVisible(true);

        authorField.setValue(book.getAuthor().getName() + ": " + book.getName());
        descriptionField.setValue(book.getDescription() != null ? book.getDescription() : "Описание отсутствует");
        ratingField.setValue(book.getStars() != null ? String.valueOf(book.getStars()) : "Нет рейтинга");
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

        CustomNotification.showNotification("Оценка сохранена!",
                NotificationVariant.LUMO_SUCCESS);

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
            return false;
        }

        if (currentUserService.getCurrentUser() == null) {
            CustomNotification.showNotification("Ошибка: пользователь не найден",
                    NotificationVariant.LUMO_ERROR);
            return false;
        }

        if (newRatingField.getValue() == null) {
            CustomNotification.showNotification("Пожалуйста, введите оценку", NotificationVariant.LUMO_WARNING);
            return false;
        }

        int newStars = newRatingField.getValue().intValue();
        if (newStars < 1 || newStars > 5) {
            CustomNotification.showNotification("Оценка должна быть от 1 до 5", NotificationVariant.LUMO_ERROR);
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
        UserBook userBook = new UserBook(currentUser, currentBook, newStars);
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
}
