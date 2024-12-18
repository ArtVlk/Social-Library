package ru.gwolk.librarysocial.AppFrontend.Presenters;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gwolk.librarysocial.AppFrontend.SubPresenters.SubscriptionUserEditorPresenter;
import ru.gwolk.librarysocial.AppFrontend.AppLayouts.MainLayout;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.SubscriptionsRepository;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.UserRepository;
import ru.gwolk.librarysocial.AppBackend.Entities.User;
import ru.gwolk.librarysocial.AppBackend.SocialServices.CurrentUserService;
import ru.gwolk.librarysocial.AppBackend.SocialServices.SubscriptionsService;

/**
 * Презентер для страницы управления подписками пользователя.
 * Этот класс отображает список подписок текущего пользователя и позволяет взаимодействовать с подписанными пользователями.
 */
@Route(value = "subscriptions", layout = MainLayout.class)
@PageTitle("Подписки")
@PermitAll
public class SubscriptionsPresenter extends VerticalLayout {
    private Grid<User> subscriptionsGrid;
    private UserRepository userRepository;
    private H1 subscriptionsTitle;

    /**
     * Конструктор, инициализирующий компоненты страницы подписок.
     * Настроен для отображения списка подписок и взаимодействия с подписанными пользователями.
     *
     * @param subscriptionsService Сервис для получения подписок и связанных с ними данных
     * @param userRepository Репозиторий пользователей для получения информации о пользователях
     * @param subscriptionUserEditorPresenter Презентер для редактирования подписок
     */
    @Autowired
    public SubscriptionsPresenter(
            SubscriptionsService subscriptionsService,
            UserRepository userRepository,
            SubscriptionUserEditorPresenter subscriptionUserEditorPresenter
    )
    {
        this.userRepository = userRepository;
        this.subscriptionsGrid = subscriptionsService.getSubscriptionsGrid();
        this.subscriptionsTitle = new H1("Ваши подписки");

        setHorizontalComponentAlignment(Alignment.CENTER, subscriptionsTitle, subscriptionsGrid);

        subscriptionsGrid.asSingleSelect().addValueChangeListener(e ->
                subscriptionUserEditorPresenter.editSubscription(e.getValue()));

        add(subscriptionsTitle, subscriptionsGrid, subscriptionUserEditorPresenter);

        subscriptionsGrid.setItems(subscriptionsService.getSubscribedUsers());

    }


}
