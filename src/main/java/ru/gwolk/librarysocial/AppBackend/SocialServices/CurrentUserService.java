package ru.gwolk.librarysocial.AppBackend.SocialServices;

import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.UserRepository;
import ru.gwolk.librarysocial.AppBackend.Entities.User;

/**
 * Сервис {@link CurrentUserService} предоставляет методы для получения информации о текущем пользователе,
 * который авторизован в системе.
 *
 * Сервис использует Spring Security для извлечения имени текущего пользователя из контекста безопасности
 * и репозиторий {@link UserRepository} для извлечения данных о пользователе из базы данных.
 *
 * {@link CurrentUserService} предоставляет два основных метода:
 * 1. {@link CurrentUserService#getCurrentUsername()} - возвращает имя текущего пользователя.
 * 2. {@link CurrentUserService#getCurrentUser()} - возвращает объект пользователя, связанный с текущей авторизацией.
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CurrentUserService {
    @Autowired
    private UserRepository userRepository;

    /**
     * Возвращает имя текущего пользователя, который авторизован в системе.
     *
     * Метод извлекает информацию о текущем пользователе из контекста безопасности,
     * предоставленного Spring Security.
     *
     * @return Имя текущего пользователя, или {@code null}, если пользователь не авторизован.
     */
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            return authentication.getName();
        }
        return null;
    }

    /**
     * Возвращает объект {@link User} для текущего авторизованного пользователя.
     *
     * Метод извлекает имя текущего пользователя из контекста безопасности и использует
     * репозиторий {@link UserRepository} для поиска объекта пользователя в базе данных.
     *
     * @return Объект {@link User} текущего авторизованного пользователя, или {@code null},
     * если пользователь не авторизован.
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            return userRepository.findByName(getCurrentUsername()).getFirst();
        }
        return null;
    }

}
