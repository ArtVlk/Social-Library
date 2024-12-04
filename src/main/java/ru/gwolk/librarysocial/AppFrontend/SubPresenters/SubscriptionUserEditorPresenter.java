package ru.gwolk.librarysocial.AppFrontend.SubPresenters;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.SubscriptionsRepository;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.UserRepository;
import ru.gwolk.librarysocial.AppBackend.CommonServices.CustomNotification;
import ru.gwolk.librarysocial.AppBackend.Entities.User;
import ru.gwolk.librarysocial.AppBackend.Entities.UserBook;
import ru.gwolk.librarysocial.AppBackend.SocialServices.CurrentUserService;
import ru.gwolk.librarysocial.AppBackend.SocialServices.SubscriptionsService;

/**
 * Презентер для отображения и редактирования информации о пользователе, на которого подписан текущий пользователь.
 * Этот класс управляет отображением информации о подписанном пользователе, его избранных книгах, обзорах и оценках.
 */
@SpringComponent
@UIScope
public class SubscriptionUserEditorPresenter extends VerticalLayout {
    private SubscriptionsRepository subscriptionsRepository;
    private UserRepository userRepository;
    private User subscribedUser;
    private TextField name;
    private Button lookFavouritesButton;
    private Button unsubscibeButton;
    private Binder<User> userBinder;
    private Grid<UserBook> favouriteBooks;
    private TextField review;
    private TextField userRating;

    /**
     * Конструктор, инициализирующий компоненты для отображения информации о подписанном пользователе.
     *
     * @param subscriptionsService Сервис для работы с подписками
     * @param subscriptionsRepository Репозиторий для работы с подписками
     * @param userRepository Репозиторий для работы с пользователями
     */
    @Autowired
    public SubscriptionUserEditorPresenter(SubscriptionsService subscriptionsService,
                                           SubscriptionsRepository subscriptionsRepository,
                                           UserRepository userRepository) {
        this.subscriptionsRepository = subscriptionsRepository;
        this.userRepository = userRepository;
        favouriteBooks = subscriptionsService.createFavouriteBooks();
        favouriteBooks.setVisible(false);

        name = new TextField("Имя");
        name.setReadOnly(true);

        review = new TextField("Обзор");
        review.setWidth("80%");
        review.setReadOnly(true);
        review.setVisible(false);

        userRating = new TextField("Оценка");
        userRating.setWidth("5%");
        userRating.setReadOnly(true);
        userRating.setVisible(false);

        lookFavouritesButton = new Button("Посмотреть избранное", VaadinIcon.BOOK.create());
        unsubscibeButton = new Button("Отписаться", VaadinIcon.UNLINK.create());

        userBinder = new Binder<>(User.class);
        userBinder.bindInstanceFields(this);

        setHorizontalComponentAlignment(Alignment.CENTER, name, lookFavouritesButton, unsubscibeButton, favouriteBooks,
                review, userRating);
        add(name, lookFavouritesButton, unsubscibeButton, favouriteBooks, review, userRating);

        unsubscibeButton.addClickListener(e -> {
            subscriptionsService.unsubscribe(subscribedUser);
            CustomNotification.showNotification("Вы отписались", NotificationVariant.LUMO_SUCCESS);
        });
        lookFavouritesButton.addClickListener(e -> subscriptionsService.fillAndShowFavouriteBooks(subscribedUser));

        favouriteBooks.asSingleSelect().addValueChangeListener(e -> editUserBook(e.getValue()));

        setSpacing(true);
        setVisible(false);
    }

    private void editUserBook(UserBook userBook) {
        if (userBook == null) {
            review.setVisible(false);
            userRating.setVisible(false);
            return;
        }

        review.setValue(userBook.getReview() == null ? "" : userBook.getReview());
        review.setVisible(true);

        userRating.setValue(userBook.getUserRating() == null ? "" : userBook.getUserRating().toString());
        userRating.setVisible(true);
    }

    /**
     * Метод для отображения информации о подписанном пользователе.
     *
     * @param user Пользователь, на которого подписан текущий пользователь
     */
    public void editSubscription(User user) {
        if (favouriteBooks.isVisible()) {
            favouriteBooks.setVisible(false);
            review.setVisible(false);
            userRating.setVisible(false);
        }

        if (user == null) {
            setVisible(true);
            return;
        }

        if (user.getId() != null) {
            this.subscribedUser = userRepository.findById(user.getId()).orElse(user);
        }
        else {
            this.subscribedUser = user;
        }

        userBinder.setBean(subscribedUser);
        setVisible(true);
    }

}
