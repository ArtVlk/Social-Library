package ru.gwolk.librarysocial.SocialServicesTests;

import com.vaadin.flow.component.grid.Grid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.UserRepository;
import ru.gwolk.librarysocial.AppBackend.Entities.Role;
import ru.gwolk.librarysocial.AppBackend.Entities.User;
import ru.gwolk.librarysocial.AppBackend.SocialServices.UsersFilterService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsersFilterServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private Grid<User> grid;
    @InjectMocks
    private UsersFilterService usersFilterService;
    private User user1;
    private User user2;
    private Collection<User> allUsers;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user1 = new User("Eminem", "rapGod", Role.USER);
        user2 = new User("Sia", "Chandelier", Role.USER);
        allUsers = Arrays.asList(user1, user2);
    }

    @Test
    public void testShowUsers_whenNameIsEmpty() {
        when(userRepository.findAll()).thenReturn(allUsers);

        usersFilterService.showUser("");

        verify(grid, times(1)).setItems(allUsers);
    }

    @Test
    public void testShowUsers_whenNameNotEmpty() {
        String name = "Eminem";
        List<User> singleList = Collections.singletonList(user1);

        when(userRepository.findByName(name)).thenReturn(singleList);

        usersFilterService.showUser(name);

        verify(grid, times(1)).setItems(singleList);
    }
}
