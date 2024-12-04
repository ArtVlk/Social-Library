package ru.gwolk.librarysocial.AppBackend.CRUDRepositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.gwolk.librarysocial.AppBackend.Entities.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностью книги.
 * <p>
 * Предоставляет методы для выполнения CRUD-операций с книгами, а также различные способы поиска и сортировки.
 * </p>
 */
@Repository
public interface BookRepository extends CrudRepository<Book, Long>{
    /**
     * Находит книги по имени.
     *
     * @param name имя книги
     * @return список книг с заданным именем
     */
    List<Book> findByName(String name);

    /**
     * Находит максимальный ID среди всех книг.
     *
     * @return максимальный ID книги
     */
    @Query("SELECT MAX(b.id) FROM Book b")
    Long findMaxId();

    /**
     * Удаляет книгу по её ID.
     *
     * @param id идентификатор книги
     */
    void deleteById(Long id);

    /**
     * Находит книгу по её ID.
     *
     * @param id идентификатор книги
     * @return Optional, содержащий найденную книгу или пустой Optional, если книга не найдена
     */
    Optional<Book> findById(Long id);

    /**
     * Находит книги, название которых содержит указанный фрагмент.
     *
     * @param namePart часть названия книги
     * @return список книг, название которых содержит заданную часть
     */
    List<Book> findByNameContaining(String namePart);

    /**
     * Находит все книги, отсортированные по имени в порядке возрастания.
     *
     * @return список книг, отсортированных по имени
     */
    List<Book> findAllByOrderByNameAsc();


    /**
     * Находит все книги, отсортированные по имени автора в порядке возрастания.
     *
     * @return список книг, отсортированных по имени автора
     */
    List<Book> findAllByOrderByAuthor_NameAsc();

    /**
     * Находит все книги, отсортированные по названию жанра в порядке возрастания.
     *
     * @return список книг, отсортированных по названию жанра
     */
    List<Book> findAllByOrderByGenre_NameAsc();

    /**
     * Находит все книги, отсортированные по количеству звёзд в порядке убывания.
     *
     * @return список книг, отсортированных по количеству звёзд
     */
    List<Book> findAllByOrderByStarsDesc();
}
