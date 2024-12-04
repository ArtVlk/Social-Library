package ru.gwolk.librarysocial.AppBackend.CRUDRepositories;

import org.springframework.stereotype.Repository;
import ru.gwolk.librarysocial.AppBackend.Entities.Author;
import org.springframework.data.repository.CrudRepository;
import ru.gwolk.librarysocial.AppBackend.Entities.Subscription;

import java.util.Optional;


/**
 * Репозиторий для работы с сущностью {@link Author}.
 * <p>
 * Предоставляет методы для выполнения CRUD-операций и поиска автора по имени.
 * </p>
 */
@Repository
public interface AuthorRepository extends CrudRepository<Author, Long>{
    /**
     * Выполняет поиск автора по имени.
     *
     * @param name имя автора
     * @return Optional, содержащий найденного автора, или пустой Optional, если автор не найден
     */
    Optional<Author> findByName(String name);
}
