package ru.gwolk.librarysocial.AppBackend.CRUDRepositories;

import org.springframework.stereotype.Repository;
import ru.gwolk.librarysocial.AppBackend.Entities.Author;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


@Repository
public interface AuthorRepository extends CrudRepository<Author, Long>{
    Optional<Author> findByName(String name);
}
