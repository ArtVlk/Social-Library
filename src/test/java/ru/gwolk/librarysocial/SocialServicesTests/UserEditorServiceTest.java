package ru.gwolk.librarysocial.SocialServicesTests;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.api.mockito.PowerMockito;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.SubscriptionsRepository;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.UserBookRepository;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.UserRepository;
import ru.gwolk.librarysocial.AppBackend.CommonServices.CustomNotification;
import ru.gwolk.librarysocial.AppBackend.Entities.Role;
import ru.gwolk.librarysocial.AppBackend.Entities.Subscription;
import ru.gwolk.librarysocial.AppBackend.Entities.User;
import ru.gwolk.librarysocial.AppBackend.SocialServices.CurrentUserService;
import ru.gwolk.librarysocial.AppBackend.SocialServices.UserEditorService;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserEditorServiceTest {
    @Mock
    private SubscriptionsRepository subscriptionsRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserBookRepository userBookRepository;
    @Mock
    private CurrentUserService currentUserService;
    @InjectMocks
    private UserEditorService userEditorService;
    private User currentUser;
    private User userToSubscribe;


    @BeforeEach
    public void setUp() {
        currentUser = new User("Robert", "Password", Role.USER);
        userToSubscribe = new User("Vantuz", "imAGangsta", Role.USER);

        //PowerMockito.mockStatic(CustomNotification.class);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSubscribe_successfulSubscribe() {
        when(currentUserService.getCurrentUser()).thenReturn(currentUser);
        when(subscriptionsRepository.findSubscriptionByUserAndSubscribedUser(currentUser, userToSubscribe))
                .thenReturn(null);

        userEditorService.subscribe(userToSubscribe);

        verify(subscriptionsRepository, times(1)).save(any(Subscription.class));
        //PowerMockito.verifyStatic(CustomNotification.class, times(1));
    }

    @Test
    public void testSubscribe_whenAlreadySubscribed() {
        Subscription findSubscription = new Subscription(currentUser, userToSubscribe);

        when(currentUserService.getCurrentUser()).thenReturn(currentUser);
        when(subscriptionsRepository.findSubscriptionByUserAndSubscribedUser(currentUser, userToSubscribe))
                .thenReturn(findSubscription);

        userEditorService.subscribe(userToSubscribe);

        verify(subscriptionsRepository, never()).save(any(Subscription.class));
    }


}
