package ru.gwolk.librarysocial.AppBackend.CRUDRepositories;

import org.springframework.stereotype.Repository;
import ru.gwolk.librarysocial.AppBackend.Entities.Genre;
import org.springframework.data.repository.CrudRepository;
import ru.gwolk.librarysocial.AppBackend.Entities.Subscription;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link Genre}.
 * <p>
 * Предоставляет методы для выполнения CRUD-операций с жанрами, а также поиск жанра по имени.
 * </p>
 */
@Repository
public interface GenreRepository extends CrudRepository<Genre, Long>{
    /**
     * Находит жанр по имени.
     *
     * @param name имя жанра
     * @return Optional, содержащий найденный жанр, или пустой Optional, если жанр не найден
     */
    Optional<Genre> findByName(String name);
}
