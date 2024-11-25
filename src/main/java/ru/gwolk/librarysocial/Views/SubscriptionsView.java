package ru.gwolk.librarysocial.Views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gwolk.librarysocial.CRUDRepositories.SubscriptionsRepository;
import ru.gwolk.librarysocial.CRUDRepositories.UserRepository;
import ru.gwolk.librarysocial.Entities.Subscription;
import ru.gwolk.librarysocial.Entities.User;
import ru.gwolk.librarysocial.Entities.PhoneNumber;
import ru.gwolk.librarysocial.Services.CurrentUserService;
import ru.gwolk.librarysocial.Widgets.MainLayout;
import ru.gwolk.librarysocial.Widgets.SubscriptionUserEditor;

import java.util.List;
import java.util.stream.Collectors;

@Route(value = "subscriptions", layout = MainLayout.class)
@PageTitle("Подписки")
@PermitAll
public class SubscriptionsView extends VerticalLayout {
    private Grid<User> subscriptionsGrid;
    private UserRepository userRepository;
    private H1 subscriptionsTitle;
    private CurrentUserService currentUserService;
    private SubscriptionsRepository subscriptionsRepository;
    @Autowired
    public SubscriptionsView(UserRepository userRepository, SubscriptionsRepository subscriptionsRepository,
                             CurrentUserService currentUserService) {
        this.userRepository = userRepository;
        this.subscriptionsRepository = subscriptionsRepository;
        this.currentUserService = currentUserService;
        this.subscriptionsGrid = new Grid<>(User.class);
        this.subscriptionsTitle = new H1("Ваши подписки");

        setSubsciptionsGrid(subscriptionsGrid);

        setHorizontalComponentAlignment(Alignment.CENTER, subscriptionsTitle, subscriptionsGrid);

        //subscriptionsGrid.asSingleSelect().addValueChangeListener(e -> SubscriptionUserEditor.editSubscription(e.getValue()));
        add(subscriptionsTitle, subscriptionsGrid);

        List<Subscription> subscriptions = subscriptionsRepository
                .findSubscriptionsByUser(currentUserService.getCurrentUser());

        List<User> subscribedUsers = subscriptions.stream()
                .map(Subscription::getSubscribedUser)
                .toList();

        subscriptionsGrid.setItems(subscribedUsers);

    }

    private void setSubsciptionsGrid(Grid<User> subscriptionsGrid) {
        subscriptionsGrid.setHeight("300px");
        subscriptionsGrid.setWidth("900px");
        subscriptionsGrid.setColumns();

        subscriptionsGrid.addColumn(User::getName).setHeader("Имя").setWidth("540px");
        subscriptionsGrid.addColumn(User::getGender).setHeader("Пол").setWidth("90px");
        subscriptionsGrid.addColumn(user -> user.getPhoneNumbers().stream()
                .map(PhoneNumber::getNumber)
                .collect(Collectors.joining(", "))).setHeader("Номера телефонов").setWidth("270px");
    }
}
