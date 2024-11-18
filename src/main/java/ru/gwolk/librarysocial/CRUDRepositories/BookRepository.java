package ru.gwolk.librarysocial.CRUDRepositories;

import org.springframework.stereotype.Repository;
import ru.gwolk.librarysocial.Entities.Book;
import org.springframework.data.repository.CrudRepository;


@Repository
public interface BookRepository extends CrudRepository<Book, Long>{
}
