package ru.gwolk.librarysocial.AppBackend.SocialServices;

import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.gwolk.librarysocial.AppBackend.Entities.PhoneNumber;
import ru.gwolk.librarysocial.AppBackend.Entities.User;
import ru.gwolk.librarysocial.AppBackend.Entities.UserBook;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.SubscriptionsRepository;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.UserBookRepository;
import ru.gwolk.librarysocial.AppBackend.Entities.Subscription;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис {@link SubscriptionsService} предназначен для управления подписками пользователей, а также для работы с их избранными книгами.
 * Сервис предоставляет функциональность для отображения подписок, отмены подписки, создания и отображения избранных книг пользователя.
 *
 * {@link SubscriptionsService} взаимодействует с репозиториями {@link SubscriptionsRepository} и {@link UserBookRepository},
 * а также использует {@link CurrentUserService} для работы с текущим пользователем.
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SubscriptionsService {
    private SubscriptionsRepository subscriptionsRepository;
    private UserBookRepository userBookRepository;
    private User me;
    private Grid<User> subscriptionsGrid;
    private Grid<UserBook> favouriteBooks;


    private CurrentUserService currentUserService;

    /**
     * Конструктор для инициализации {@link SubscriptionsService}.
     *
     * @param subscriptionsRepository Репозиторий для работы с подписками.
     * @param currentUserService Сервис для получения текущего пользователя.
     * @param userBookRepository Репозиторий для работы с книгами пользователя.
     */
    @Autowired
    public SubscriptionsService(SubscriptionsRepository subscriptionsRepository,
                                CurrentUserService currentUserService,
                                UserBookRepository userBookRepository) {
        this.currentUserService = currentUserService;
        this.subscriptionsRepository = subscriptionsRepository;
        this.userBookRepository = userBookRepository;
        createAndSetSubsciptionsGrid();
    }

    /**
     * Отменяет подписку на указанного пользователя.
     *
     * Метод находит подписку между текущим пользователем и указанным пользователем.
     * Если подписка существует, она удаляется.
     *
     * @param subscribedUser Пользователь, на которого нужно отменить подписку.
     * @throws IllegalArgumentException если подписка не найдена.
     */
    public void unsubscribe(User subscribedUser) {
        me = getUser();
        Subscription subscription = subscriptionsRepository.findSubscriptionByUserAndSubscribedUser(me, subscribedUser);

        if (subscription == null) {
            throw new IllegalArgumentException("Подписка не найдена");
        }

        subscriptionsRepository.delete(subscription);
        subscriptionsGrid.setItems(subscriptionsRepository.findAllSubscribedUsers());
    }

    private void createAndSetSubsciptionsGrid() {
        subscriptionsGrid = new Grid<>(User.class);
        subscriptionsGrid.setHeight("300px");
        subscriptionsGrid.setWidth("900px");
        subscriptionsGrid.setColumns();

        subscriptionsGrid.addColumn(User::getName).setHeader("Имя").setWidth("540px")
                .setTextAlign(ColumnTextAlign.CENTER);;
        subscriptionsGrid.addColumn(User::getGender).setHeader("Пол").setWidth("90px")
                .setTextAlign(ColumnTextAlign.CENTER);;
    }

    /**
     * Создает и настраивает таблицу с избранными книгами пользователя.
     *
     * @return Настроенная таблица с книгами.
     */
    public Grid<UserBook> createFavouriteBooks() {
        favouriteBooks = new Grid<>(UserBook.class);
        favouriteBooks.setHeight("300px");
        favouriteBooks.setWidth("900px");
        favouriteBooks.setColumns();

        favouriteBooks.addColumn(userBook -> userBook.getBook().getName()).setHeader("Название")
                .setWidth("225px").setTextAlign(ColumnTextAlign.CENTER);;
        favouriteBooks.addColumn(userBook -> userBook.getBook().getAuthor().getName()).setHeader("Автор")
                .setWidth("225px").setTextAlign(ColumnTextAlign.CENTER);;
        favouriteBooks.addColumn(userBook -> userBook.getBook().getGenre().getName()).setHeader("Жанр")
                .setWidth("225px").setTextAlign(ColumnTextAlign.CENTER);;
        favouriteBooks.addColumn(UserBook::getUserRating).setHeader("Оценка")
                .setWidth("225px").setTextAlign(ColumnTextAlign.CENTER);;

        favouriteBooks.getElement().getStyle().set("margin-left", "auto");
        favouriteBooks.getElement().getStyle().set("margin-right", "auto");

        return favouriteBooks;
    }

    /**
     * Заполняет таблицу избранных книг и делает её видимой.
     *
     * Метод заполняет таблицу favouriteBooks книгами, связанными с указанным пользователем.
     *
     * @param subscribedUser Пользователь, чьи избранные книги нужно отобразить.
     */
    public void fillAndShowFavouriteBooks(User subscribedUser) {
        favouriteBooks.setItems(userBookRepository.findByUser(subscribedUser));
        favouriteBooks.setVisible(true);
    }


    public Grid<User> getSubscriptionsGrid() {
        return subscriptionsGrid;
    }

    private User getUser() {
        if (me == null) {
            me = currentUserService.getCurrentUser();
        }
        return me;
    }

    public List<User> getSubscribedUsers() {
        List<Subscription> subscriptions = subscriptionsRepository
                .findSubscriptionsByUser(currentUserService.getCurrentUser());

        return subscriptions.stream()
                .map(Subscription::getSubscribedUser)
                .toList();
    }


}
