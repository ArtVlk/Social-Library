package ru.gwolk.librarysocial.CRUDRepositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.gwolk.librarysocial.Entities.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


@Repository
public interface BookRepository extends CrudRepository<Book, Long>{
    List<Book> findByName(String name);

    @Query("SELECT MAX(b.id) FROM Book b")
    Long findMaxId();
}
