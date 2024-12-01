package ru.gwolk.librarysocial.AppBackend.CRUDRepositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.gwolk.librarysocial.AppBackend.Entities.Book;
import ru.gwolk.librarysocial.AppBackend.Entities.User;
import ru.gwolk.librarysocial.AppBackend.Entities.UserBook;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserBookRepository extends CrudRepository<UserBook, Long> {
    Optional<UserBook> findByUserAndBook(User user, Book book);
    Collection<UserBook> findByUserAndBookNameContaining(User user, String name);
    List<UserBook> findByUser(User user);
    void deleteByBookId(Long bookId);
}
