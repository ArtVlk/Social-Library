package ru.gwolk.librarysocial.SocialServicesTests;

import com.google.common.base.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.UserRepository;
import ru.gwolk.librarysocial.AppBackend.Entities.User;
import ru.gwolk.librarysocial.AppBackend.SocialServices.CurrentUserService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrentUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CurrentUserService currentUserService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCurrentUsername_whenAuthenticated() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String username = currentUserService.getCurrentUsername();

        assertEquals("testUser", username);
    }

    @Test
    public void testGetCurrentUsername_whenNotAuthenticated() {
        SecurityContextHolder.clearContext();

        String username = currentUserService.getCurrentUsername();

        assertNull(username);
    }

    @Test
    public void testGetCurrentUser_whenAuthenticated() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = new User();
        user.setName("testUser");
        when(userRepository.findByName("testUser")).thenReturn(List.of(user));

        User currentUser = currentUserService.getCurrentUser();

        assertNotNull(currentUser);
        assertEquals("testUser", currentUser.getName());
    }

    @Test
    public void testGetCurrentUser_whenNotAuthenticated() {
        SecurityContextHolder.clearContext();

        User currentUser = currentUserService.getCurrentUser();

        assertNull(currentUser);
    }
}
