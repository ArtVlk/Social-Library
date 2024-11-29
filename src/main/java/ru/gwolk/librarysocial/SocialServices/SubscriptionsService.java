package ru.gwolk.librarysocial.SocialServices;

import com.vaadin.flow.component.grid.Grid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gwolk.librarysocial.CRUDRepositories.SubscriptionsRepository;
import ru.gwolk.librarysocial.Entities.PhoneNumber;
import ru.gwolk.librarysocial.Entities.Subscription;
import ru.gwolk.librarysocial.Entities.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionsService {
    private SubscriptionsRepository subscriptionsRepository;
    private User me;
    private Grid<User> subscriptionsGrid;

    private CurrentUserService currentUserService;
    @Autowired
    public SubscriptionsService(SubscriptionsRepository subscriptionsRepository,
                                CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
        this.subscriptionsRepository = subscriptionsRepository;
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
