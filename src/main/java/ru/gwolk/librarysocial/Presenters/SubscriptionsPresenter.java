package ru.gwolk.librarysocial.Presenters;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.gwolk.librarysocial.CRUDRepositories.SubscriptionsRepository;
import ru.gwolk.librarysocial.CRUDRepositories.UserRepository;
import ru.gwolk.librarysocial.Entities.Subscription;
import ru.gwolk.librarysocial.Entities.User;
import ru.gwolk.librarysocial.Entities.PhoneNumber;
import ru.gwolk.librarysocial.SocialServices.CurrentUserService;
import ru.gwolk.librarysocial.AppLayouts.MainLayout;
import ru.gwolk.librarysocial.SocialServices.SubscriptionsService;
import ru.gwolk.librarysocial.SubPresenters.SubscriptionUserEditorPresenter;

import java.util.List;
import java.util.stream.Collectors;

@Route(value = "subscriptions", layout = MainLayout.class)
@PageTitle("Подписки")
@PermitAll
public class SubscriptionsPresenter extends VerticalLayout {
    private Grid<User> subscriptionsGrid;
    private UserRepository userRepository;
    private H1 subscriptionsTitle;
    private CurrentUserService currentUserService;
    private SubscriptionsRepository subscriptionsRepository;
    private SubscriptionUserEditorPresenter subscriptionUserEditorPresenter;
    private SubscriptionsService subscriptionsService;
    @Autowired
    public SubscriptionsPresenter(
            SubscriptionsService subscriptionsService,
            UserRepository userRepository,
            SubscriptionsRepository subscriptionsRepository,
            CurrentUserService currentUserService,
            SubscriptionUserEditorPresenter subscriptionUserEditorPresenter
    )
    {
        this.subscriptionsService = subscriptionsService;
        this.userRepository = userRepository;
        this.subscriptionsRepository = subscriptionsRepository;
        this.subscriptionUserEditorPresenter = subscriptionUserEditorPresenter;
        this.currentUserService = currentUserService;
        this.subscriptionsGrid = subscriptionsService.getSubscriptionsGrid();
        this.subscriptionsTitle = new H1("Ваши подписки");

        setHorizontalComponentAlignment(Alignment.CENTER, subscriptionsTitle, subscriptionsGrid);

        subscriptionsGrid.asSingleSelect().addValueChangeListener(e ->
                subscriptionUserEditorPresenter.editSubscription(e.getValue()));

        add(subscriptionsTitle, subscriptionsGrid, subscriptionUserEditorPresenter);

        subscriptionsGrid.setItems(subscriptionsService.getSubscribedUsers());

    }


}
