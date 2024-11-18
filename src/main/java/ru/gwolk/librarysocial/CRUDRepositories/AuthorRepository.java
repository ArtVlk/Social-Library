package ru.gwolk.librarysocial.CRUDRepositories;

import org.springframework.stereotype.Repository;
import ru.gwolk.librarysocial.Entities.Author;
import org.springframework.data.repository.CrudRepository;


@Repository
public interface AuthorRepository extends CrudRepository<Author, Long>{
}
