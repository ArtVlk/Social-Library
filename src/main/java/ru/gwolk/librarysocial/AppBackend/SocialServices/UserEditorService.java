package ru.gwolk.librarysocial.AppBackend.SocialServices;

import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.UserRepository;
import ru.gwolk.librarysocial.AppBackend.CommonServices.CustomNotification;
import ru.gwolk.librarysocial.AppBackend.Entities.PhoneNumber;
import ru.gwolk.librarysocial.AppBackend.Entities.User;
import ru.gwolk.librarysocial.AppBackend.Entities.UserBook;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.SubscriptionsRepository;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.UserBookRepository;
import ru.gwolk.librarysocial.AppBackend.Entities.Subscription;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserEditorService {
    private CurrentUserService currentUserService;
    private SubscriptionsRepository subscriptionsRepository;
    private UserRepository userRepository;
    private UserBookRepository userBookRepository;
    private Notification notificationOk;
    private Notification notificationWarning;
    private Notification notificationBan;
    private User me;
    private Grid<User> usersGrid;

    @Autowired
    public UserEditorService(CurrentUserService currentUserService,
                             SubscriptionsRepository subscriptionsRepository,
                             UserRepository userRepository,
                             UserBookRepository userBookRepository) {
        this.userRepository = userRepository;
        this.userBookRepository = userBookRepository;
        this.currentUserService = currentUserService;
        this.subscriptionsRepository = subscriptionsRepository;
        createAndSetUsersGrid();
    }

    private void createAndSetUsersGrid() {
        usersGrid = new Grid<>(User.class);
        usersGrid.setHeight("400px");
        usersGrid.setWidth("900px");
        usersGrid.setColumns();

        usersGrid.addColumn(User::getName).setHeader("Имя").setWidth("80%").setTextAlign(ColumnTextAlign.CENTER);
        usersGrid.addColumn(User::getGender).setHeader("Пол").setWidth("20%").setTextAlign(ColumnTextAlign.CENTER);

        usersGrid.getElement().getStyle().set("margin-left", "auto");
        usersGrid.getElement().getStyle().set("margin-right", "auto");
    }

    public Grid<User> getUsersGrid() {
        return usersGrid;
    }

    private User getUser() {
        if (me == null) {
            me = currentUserService.getCurrentUser();
        }
        return me;
    }

    public void subscribe(User user) {
        me = getUser();
        Subscription findingSubscription = subscriptionsRepository.findSubscriptionByUserAndSubscribedUser(me, user);

        if (findingSubscription == null) {
            Subscription subscription = new Subscription(me, user);
            subscriptionsRepository.save(subscription);
            CustomNotification.showNotification("✓", NotificationVariant.LUMO_SUCCESS);
        }
        else {
            CustomNotification.showNotification("Вы уже подписаны на этого пользователя",
                    NotificationVariant.LUMO_WARNING);
        }
    }


    public void ban(User editingUser) {
        List<Subscription> editingUserSubscriptions = subscriptionsRepository.findSubscriptionsByUser(editingUser);
        editingUserSubscriptions.forEach(s -> subscriptionsRepository.delete(s));

        List<Subscription> onEditingUserSubscriptions = subscriptionsRepository.
                findSubscriptionsBySubscribedUser(editingUser);
        onEditingUserSubscriptions.forEach(s -> subscriptionsRepository.delete(s));

        List<UserBook> editingUserBooks = userBookRepository.findByUser(editingUser);
        editingUserBooks.forEach(u -> userBookRepository.delete(u));

        userRepository.delete(editingUser);
        usersGrid.setItems((Collection<User>) userRepository.findAll());

        CustomNotification.showNotification("Забанен!", NotificationVariant.LUMO_ERROR);
    }

}
