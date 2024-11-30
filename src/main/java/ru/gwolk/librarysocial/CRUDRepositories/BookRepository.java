package ru.gwolk.librarysocial.CRUDRepositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.gwolk.librarysocial.Entities.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BookRepository extends CrudRepository<Book, Long>{
    List<Book> findByName(String name);

    @Query("SELECT MAX(b.id) FROM Book b")
    Long findMaxId();

    void deleteById(Long id);
    Optional<Book> findById(Long id);

    List<Book> findByNameContaining(String namePart);

}
