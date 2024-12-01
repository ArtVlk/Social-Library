package ru.gwolk.librarysocial.SocialServices;

import com.vaadin.flow.component.grid.Grid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gwolk.librarysocial.CRUDRepositories.SubscriptionsRepository;
import ru.gwolk.librarysocial.CRUDRepositories.UserBookRepository;
import ru.gwolk.librarysocial.Entities.PhoneNumber;
import ru.gwolk.librarysocial.Entities.Subscription;
import ru.gwolk.librarysocial.Entities.User;
import ru.gwolk.librarysocial.Entities.UserBook;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionsService {
    private SubscriptionsRepository subscriptionsRepository;
    private UserBookRepository userBookRepository;
    private User me;
    private Grid<User> subscriptionsGrid;
    private Grid<UserBook> favouriteBooks;

    private CurrentUserService currentUserService;
    @Autowired
    public SubscriptionsService(SubscriptionsRepository subscriptionsRepository,
                                CurrentUserService currentUserService,
                                UserBookRepository userBookRepository) {
        this.currentUserService = currentUserService;
        this.subscriptionsRepository = subscriptionsRepository;
        this.userBookRepository = userBookRepository;
        createAndSetSubsciptionsGrid();
    }

    public void unsubscribe(User subscibedUser) {
        me = getUser();
        Subscription subscription = subscriptionsRepository.findSubscriptionByUserAndSubscribedUser(me, subscibedUser);
        subscriptionsRepository.delete(subscription);
        subscriptionsGrid.setItems(subscriptionsRepository.findAllSubscribedUsers());
    }

    private void createAndSetSubsciptionsGrid() {
        subscriptionsGrid = new Grid<>(User.class);
        subscriptionsGrid.setHeight("300px");
        subscriptionsGrid.setWidth("900px");
        subscriptionsGrid.setColumns();

        subscriptionsGrid.addColumn(User::getName).setHeader("Имя").setWidth("540px");
        subscriptionsGrid.addColumn(User::getGender).setHeader("Пол").setWidth("90px");
        subscriptionsGrid.addColumn(user -> user.getPhoneNumbers().stream()
                .map(PhoneNumber::getNumber)
                .collect(Collectors.joining(", "))).setHeader("Номера телефонов").setWidth("270px");
    }

    public Grid<UserBook> createFavouriteBooks() {
        favouriteBooks = new Grid<>(UserBook.class);
        favouriteBooks.setHeight("300px");
        favouriteBooks.setWidth("900px");
        favouriteBooks.setColumns();

        favouriteBooks.addColumn(userBook -> userBook.getBook().getName()).setHeader("Название").setWidth("225px");
        favouriteBooks.addColumn(userBook -> userBook.getBook().getAuthor().getName()).setHeader("Автор").setWidth("225px");
        favouriteBooks.addColumn(userBook -> userBook.getBook().getGenre().getName()).setHeader("Жанр").setWidth("225px");
        favouriteBooks.addColumn(UserBook::getUserRating).setHeader("Оценка").setWidth("225px");

        favouriteBooks.getElement().getStyle().set("margin-left", "auto");
        favouriteBooks.getElement().getStyle().set("margin-right", "auto");

        return favouriteBooks;
    }
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
