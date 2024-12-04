package ru.gwolk.librarysocial.AppBackend.CRUDRepositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.gwolk.librarysocial.AppBackend.Entities.Book;
import ru.gwolk.librarysocial.AppBackend.Entities.User;
import ru.gwolk.librarysocial.AppBackend.Entities.UserBook;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностью книги, связанной с пользователем (UserBook).
 * <p>
 * Предоставляет методы для выполнения CRUD-операций с пользовательскими книгами, а также различные способы поиска
 * и сортировки по критериям пользователя и книги.
 * </p>
 */
@Repository
public interface UserBookRepository extends CrudRepository<UserBook, Long> {
    /**
     * Находит запись о пользователе и книге по пользователю и книге.
     *
     * @param user пользователь
     * @param book книга
     * @return Optional, содержащий найденную запись о пользователе и книге, или пустой Optional, если запись не найдена
     */
    Optional<UserBook> findByUserAndBook(User user, Book book);

    /**
     * Находит записи о пользователе и книгах, название которых содержит указанный фрагмент.
     *
     * @param user пользователь
     * @param name часть названия книги
     * @return коллекция записей о пользователе и книгах
     */
    Collection<UserBook> findByUserAndBookNameContaining(User user, String name);

    /**
     * Находит все записи о книгах для пользователя.
     *
     * @param user пользователь
     * @return список всех записей о книгах для пользователя
     */
    List<UserBook> findByUser(User user);

    /**
     * Удаляет все записи о книгах для указанной книги по её ID.
     *
     * @param bookId идентификатор книги
     */
    void deleteByBookId(Long bookId);

    /**
     * Находит все записи о книгах для пользователя, отсортированные по названию книги в порядке возрастания.
     *
     * @param user пользователь
     * @return коллекция записей о книгах, отсортированных по названию
     */
    Collection<UserBook> findByUserOrderByBook_NameAsc(User user);

    /**
     * Находит все записи о книгах для пользователя, отсортированные по имени автора книги в порядке возрастания.
     *
     * @param user пользователь
     * @return коллекция записей о книгах, отсортированных по имени автора
     */
    Collection<UserBook> findByUserOrderByBook_Author_NameAsc(User user);

    /**
     * Находит все записи о книгах для пользователя, отсортированные по названию жанра книги в порядке возрастания.
     *
     * @param user пользователь
     * @return коллекция записей о книгах, отсортированных по названию жанра
     */
    Collection<UserBook> findByUserOrderByBook_Genre_NameAsc(User user);

    /**
     * Находит все записи о книгах для пользователя, отсортированные по рейтингу книги в порядке убывания.
     *
     * @param user пользователь
     * @return коллекция записей о книгах, отсортированных по рейтингу книги
     */
    Collection<UserBook> findByUserOrderByUserRatingDesc(User user);
}
