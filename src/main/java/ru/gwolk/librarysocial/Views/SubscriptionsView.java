package ru.gwolk.librarysocial.Views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gwolk.librarysocial.CRUDRepositories.UserRepository;
import ru.gwolk.librarysocial.Entities.User;
import ru.gwolk.librarysocial.Entities.PhoneNumber;

import java.util.stream.Collectors;

@Route(value = "subscriptions", layout = MainLayout.class)
@PageTitle("Подписки")
public class SubscriptionsView extends VerticalLayout {
    private Grid<User> subscriptionsGrid;
    private UserRepository userRepository;
    private H1 subscriptionsTitle;
    @Autowired
    public SubscriptionsView(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.subscriptionsGrid = new Grid<>(User.class);
        this.subscriptionsTitle = new H1("Ваши подписки");

        subscriptionsGrid.setHeight("300px");
        subscriptionsGrid.setWidth("900px");
        subscriptionsGrid.setColumns();

        subscriptionsGrid.addColumn(User::getName).setHeader("Имя").setWidth("540px");
        subscriptionsGrid.addColumn(User::getGender).setHeader("Пол").setWidth("90px");
        subscriptionsGrid.addColumn(user -> user.getPhoneNumbers().stream()
                .map(PhoneNumber::getNumber)
                .collect(Collectors.joining(", "))).setHeader("Номера телефонов").setWidth("270px");

        add(subscriptionsTitle, subscriptionsGrid);

        subscriptionsGrid.setItems(userRepository.findByGender("M"));

    }
}
