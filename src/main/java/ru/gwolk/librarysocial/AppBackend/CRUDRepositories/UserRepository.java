package ru.gwolk.librarysocial.AppBackend.CRUDRepositories;

import org.springframework.stereotype.Repository;
import ru.gwolk.librarysocial.AppBackend.Entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;



import java.util.List;

/**
 * Репозиторий для работы с сущностью пользователя.
 * <p>
 * Предоставляет методы для выполнения CRUD-операций с пользователями, а также поиск пользователей по имени, полу и стране.
 * </p>
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    /**
     * Находит пользователей по имени.
     *
     * @param name имя пользователя
     * @return список пользователей с заданным именем
     */
    List<User> findByName(String name);

    /**
     * Находит пользователей по полу.
     *
     * @param gender пол пользователя
     * @return список пользователей с заданным полом
     */
    List<User> findByGender(String gender);

    /**
     * Находит пользователей по стране.
     *
     * @param country название страны
     * @return список пользователей, проживающих в заданной стране
     */
    @Query("SELECT c FROM User c WHERE c.country.name = :country")
    List<User> findByCountry(@Param("country") String country);

}
