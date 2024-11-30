package ru.gwolk.librarysocial.SubPresenters;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import ru.gwolk.librarysocial.CRUDRepositories.BookRepository;
import ru.gwolk.librarysocial.CRUDRepositories.UserBookRepository;
import ru.gwolk.librarysocial.CRUDRepositories.UserRepository;
import ru.gwolk.librarysocial.Entities.Book;
import ru.gwolk.librarysocial.Entities.User;
import ru.gwolk.librarysocial.Entities.UserBook;
import ru.gwolk.librarysocial.SocialServices.CurrentUserService;

import java.util.Optional;

@SpringComponent
@UIScope
public class PersonalBookDetailPresenter extends VerticalLayout {
    private final UserBookRepository userBookRepository;
    private final BookRepository bookRepository;
    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;
    private UserBook currentUserBook;
    private Book currentBook;

    private final TextField authorField = new TextField("Автор и название");
    private final TextField ratingField = new TextField("Ваша оценка");
    private final TextField userReviewField = new TextField("Отзыв");
    private Button saveRatingButton = new Button("Сохранить оценку");
    private Button saveUserReviewButton = new Button ("Сохранить отзыв");
    private Button deleteFromUserLibraryButton = new Button("Удалить");
    private Button cancelButton = new Button("Отмена");

    public PersonalBookDetailPresenter(BookRepository bookRepository, UserBookRepository userBookRepository,
                                       CurrentUserService currentUserService, UserRepository userRepository) {
        this.userBookRepository = userBookRepository;
        this.bookRepository = bookRepository;
        this.currentUserService = currentUserService;
        this.userRepository = userRepository;

        userReviewField.setWidth("80%");
        userReviewField.setHeight("200px");

        authorField.setWidth("80%");

        HorizontalLayout actionsLayout = new HorizontalLayout(deleteFromUserLibraryButton, cancelButton);
        setHorizontalComponentAlignment(Alignment.CENTER, authorField, userReviewField, saveUserReviewButton, ratingField,
                saveRatingButton, actionsLayout);
        add(authorField, userReviewField, ratingField, saveUserReviewButton, saveRatingButton, actionsLayout);

        authorField.setReadOnly(true);

        saveRatingButton.addClickListener(e -> saveRating());
        saveUserReviewButton.addClickListener(e -> saveUserReview());
        deleteFromUserLibraryButton.addClickListener(e -> deleteFromUserLibrary());
        cancelButton.addClickListener(e -> cancelWatching());
    }

    private void cancelWatching() {
        setVisible(false);
    }

    private void saveRating() {
        if (currentBook == null) {
            Notification.show("Ошибка: книга не выбрана");
            return;
        }

        User currentUser = currentUserService.getCurrentUser();
        if (currentUser == null) {
            Notification.show("Ошибка: пользователь не найден");
            return;
        }

        int newRating;
        try {
            newRating = Integer.parseInt(ratingField.getValue());
            if (newRating < 1 || newRating > 5) {
                Notification.show("Оценка должна быть в диапазоне от 1 до 5");
                return;
            }
        } catch (NumberFormatException e) {
            Notification.show("Ошибка: некорректный формат оценки");
            return;
        }

        if (currentBook.getSumStars() == null) {
            currentBook.setSumStars(currentBook.getStars());
        }
        if (currentBook.getEstimationCount() == null) {
            currentBook.setEstimationCount(1);
        }

        Optional<UserBook> userBookOptional = userBookRepository.findByUserAndBook(currentUser, currentBook);
        if (userBookOptional.isPresent()) {
            UserBook userBook = userBookOptional.get();

            if (userBook.getUserRating() != null) {
                currentBook.setSumStars(currentBook.getSumStars() - userBook.getUserRating());
            } else {
                currentBook.setEstimationCount(currentBook.getEstimationCount() + 1);
            }

            userBook.setUserRating(newRating);
            currentBook.setSumStars(currentBook.getSumStars() + newRating);

            if (currentBook.getEstimationCount() > 0) {
                currentBook.setStars(currentBook.getSumStars() / currentBook.getEstimationCount());
            } else {
                currentBook.setStars(0);
            }

            userBookRepository.save(userBook);
            bookRepository.save(currentBook);

            Notification.show("Оценка сохранена!");
        } else {
            Notification.show("Книга не найдена в вашей библиотеке.");
        }
    }


    private void saveUserReview() {
        if (currentBook == null) {
            Notification.show("Ошибка: книга не выбрана");
            return;
        }
        if (currentUserService.getCurrentUser() == null) {
            Notification.show("Ошибка: пользователь не найден");
            return;
        }

        User currentUser = currentUserService.getCurrentUser();
        Optional<UserBook> userBookOptional = userBookRepository.findByUserAndBook(currentUser, currentBook);

        if (userBookOptional.isPresent()) {
            UserBook userBook = userBookOptional.get();
            userBook.setReview(userReviewField.getValue());
            userBookRepository.save(userBook);
            Notification.show("Отзыв сохранен!");
        } else {
            Notification.show("Отзыв не удалось сохранить, так как книга не найдена в библиотеке пользователя.");
        }
    }

    private void deleteFromUserLibrary() {
        if (currentBook == null) {
            Notification.show("Ошибка: книга не выбрана");
            return;
        }

        User currentUser = currentUserService.getCurrentUser();
        if (currentUser == null) {
            Notification.show("Ошибка: пользователь не найден");
            return;
        }

        Optional<UserBook> userBookOptional = userBookRepository.findByUserAndBook(currentUser, currentBook);
        if (userBookOptional.isPresent()) {
            UserBook userBook = userBookOptional.get();
            removeUserRatingFromBook(userBook);
            userBookRepository.delete(userBook);
            //userBook.

            Notification.show("Книга удалена из вашей библиотеки.");
        } else {
            Notification.show("Книга не найдена в вашей библиотеке.");
        }
    }

    private void removeUserRatingFromBook(UserBook userBook) {
        if (userBook.getUserRating() != null) {
            int ratingToRemove = userBook.getUserRating();
            if (currentBook.getSumStars() != null && currentBook.getEstimationCount() != null) {
                currentBook.setSumStars(currentBook.getSumStars() - ratingToRemove);
                currentBook.setEstimationCount(currentBook.getEstimationCount() - 1);
                if (currentBook.getEstimationCount() > 0) {
                    currentBook.setStars(currentBook.getSumStars() / currentBook.getEstimationCount());
                } else {
                    currentBook.setStars(0);
                }
            }
        }
        bookRepository.save(currentBook);
    }

    public void editBook(UserBook userBook) {
        if (userBook == null || userBook.getBook() == null) {
            setVisible(false);
            return;
        }
        this.currentUserBook = userBook;
        this.currentBook = userBook.getBook();
        setVisible(true);

        authorField.setValue(currentBook.getAuthor().getName() + ": " + currentBook.getName());
        userReviewField.setValue(userBook.getReview() != null ? userBook.getReview() : "Отзыв отсутствует");
        ratingField.setValue(userBook.getUserRating() != null ? String.valueOf(userBook.getUserRating()) : "");
    }
}
