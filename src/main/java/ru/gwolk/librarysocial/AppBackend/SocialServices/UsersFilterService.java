package ru.gwolk.librarysocial.AppBackend.SocialServices;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.spring.annotation.UIScope;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.UserRepository;
import ru.gwolk.librarysocial.AppBackend.Entities.User;

import java.util.Collection;

/**
 * Сервис для фильтрации пользователей по имени и отображения их в сетке.
 * Предоставляет функционал для поиска пользователей по имени и отображения результатов в интерфейсе.
 */
public class UsersFilterService {

    private UserRepository userRepository;
    private Grid<User> grid;

    /**
     * Конструктор для инициализации сервиса фильтрации пользователей.
     *
     * @param grid сетка для отображения пользователей
     * @param userRepository репозиторий для работы с пользователями
     */
    public UsersFilterService(Grid<User> grid, UserRepository userRepository) {
        this.grid = grid;
        this.userRepository = userRepository;
    }

    /**
     * Фильтрует пользователей по имени и отображает их в сетке.
     * Если имя пустое, отображаются все пользователи.
     *
     * @param name имя для фильтрации пользователей
     */
    public void showUser(String name) {
        if (name.isEmpty()) {
            grid.setItems((Collection<User>) userRepository.findAll());
        }
        else {
            grid.setItems(userRepository.findByName(name));
        }
    }
}
