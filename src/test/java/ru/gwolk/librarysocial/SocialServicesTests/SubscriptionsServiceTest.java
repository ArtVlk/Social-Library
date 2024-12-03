package ru.gwolk.librarysocial.SocialServicesTests;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.SubscriptionsRepository;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.UserBookRepository;
import ru.gwolk.librarysocial.AppBackend.Entities.Subscription;
import ru.gwolk.librarysocial.AppBackend.Entities.User;
import ru.gwolk.librarysocial.AppBackend.Entities.UserBook;
import ru.gwolk.librarysocial.AppBackend.SocialServices.CurrentUserService;
import ru.gwolk.librarysocial.AppBackend.SocialServices.SubscriptionsService;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubscriptionsServiceTest {

    @Mock
    private SubscriptionsRepository subscriptionsRepository;

    @Mock
    private UserBookRepository userBookRepository;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private SubscriptionsService subscriptionsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUnsubscribe_SuccessfulUnsubscribe() {
        User currentUser = new User();
        User subscribedUser = new User();
        Subscription subscription = new Subscription(currentUser, subscribedUser);

        when(currentUserService.getCurrentUser()).thenReturn(currentUser);
        when(subscriptionsRepository.findSubscriptionByUserAndSubscribedUser(currentUser, subscribedUser))
                .thenReturn(subscription);

        subscriptionsService.unsubscribe(subscribedUser);

        verify(subscriptionsRepository, times(1)).delete(subscription);
    }

    @Test
    public void testUnsubscribe_SubscriptionNotFound() {
        User currentUser = new User();
        User subscribedUser = new User();

        when(currentUserService.getCurrentUser()).thenReturn(currentUser);
        when(subscriptionsRepository.findSubscriptionByUserAndSubscribedUser(currentUser, subscribedUser))
                .thenReturn(null);

        try {
            subscriptionsService.unsubscribe(subscribedUser);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Подписка не найдена", e.getMessage());
        }
    }

    @Test
    public void testGetSubscribedUsers_ReturnsSubscribedUsers() {
        User currentUser = new User();
        User subscribedUser1 = new User();
        User subscribedUser2 = new User();
        Subscription subscription1 = new Subscription(currentUser, subscribedUser1);
        Subscription subscription2 = new Subscription(currentUser, subscribedUser2);

        when(currentUserService.getCurrentUser()).thenReturn(currentUser);
        when(subscriptionsRepository.findSubscriptionsByUser(currentUser))
                .thenReturn(Collections.singletonList(subscription1));

        List<User> subscribedUsers = subscriptionsService.getSubscribedUsers();

        assertEquals(1, subscribedUsers.size());
        assertTrue(subscribedUsers.contains(subscribedUser1));
    }
}
