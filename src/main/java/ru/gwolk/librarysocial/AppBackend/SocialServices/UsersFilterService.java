package ru.gwolk.librarysocial.AppBackend.SocialServices;

import com.vaadin.flow.component.grid.Grid;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.UserRepository;
import ru.gwolk.librarysocial.AppBackend.Entities.User;

import java.util.Collection;


public class UsersFilterService {

    private UserRepository userRepository;
    /*@Autowired
    public UsersFilterService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }*/
    private Grid<User> grid;
    public UsersFilterService(Grid<User> grid, UserRepository userRepository) {
        this.grid = grid;
        this.userRepository = userRepository;
    }
    public void showUser(String name) {
        if (name.isEmpty()) {
            grid.setItems((Collection<User>) userRepository.findAll());
        }
        else {
            grid.setItems(userRepository.findByName(name));
        }
    }
}
