package ru.gwolk.librarysocial.SocialServices;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gwolk.librarysocial.CRUDRepositories.SubscriptionsRepository;
import ru.gwolk.librarysocial.CRUDRepositories.UserRepository;
import ru.gwolk.librarysocial.Entities.Subscription;
import ru.gwolk.librarysocial.Entities.User;
import ru.gwolk.librarysocial.SubPresenters.UserEditorPresenter;

public class UserEditorService {
    private CurrentUserService currentUserService;
    private SubscriptionsRepository subscriptionsRepository;
    private UserRepository userRepository;
    private Notification notificationOk;
    private Notification notificationWarning;
    private User me;
    private Grid<User> subscriptionsGrid;
    private Grid<User> usersGrid;
    private UsersFilterService usersFilterService;

    public UserEditorService(CurrentUserService currentUserService, SubscriptionsRepository subscriptionsRepository
    ) {
        this.currentUserService = currentUserService;
        this.subscriptionsRepository = subscriptionsRepository;
        this.me = currentUserService.getCurrentUser();
    }

    /*public UserEditorService(CurrentUserService currentUserService, SubscriptionsRepository subscriptionsRepository) {
        this.currentUserService = currentUserService;
        this.subscriptionsRepository = subscriptionsRepository;
        this.me = currentUserService.getCurrentUser();
    }

    public UserEditorService(UserEditorPresenter userEditorPresenter,
                             CurrentUserService currentUserService,
                             UserRepository userRepository,
                             Grid<User> usersGrid
                             ) {
        this.currentUserService = currentUserService;
        this.userRepository = userRepository;
        this.me = currentUserService.getCurrentUser();
        this.userEditorPresenter = userEditorPresenter;
        this.usersGrid = usersGrid;
        usersFilterService = new UsersFilterService(usersGrid, userRepository);
    }*/

    public void subscribe(User user) {
        Subscription findingSubscription = subscriptionsRepository.findSubscriptionByUserAndSubscribedUser(me, user);

        if (findingSubscription == null) {
            Subscription subscription = new Subscription(me, user);
            subscriptionsRepository.save(subscription);
            notificationOk = Notification.show("✓");
            notificationOk.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        }
        else {
            notificationWarning = Notification.show("Вы уже подписаны на этого пользователя");
            notificationWarning.addThemeVariants(NotificationVariant.LUMO_WARNING);
        }
    }



    /*public void onChangeUsersList() {
        userEditorPresenter.setVisible(false);
        usersFilterService.showUser(filter.getValue());
    }*/


}
