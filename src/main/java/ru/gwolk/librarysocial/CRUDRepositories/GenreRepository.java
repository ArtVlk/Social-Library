package ru.gwolk.librarysocial.CRUDRepositories;

import org.springframework.stereotype.Repository;
import ru.gwolk.librarysocial.Entities.Genre;
import org.springframework.data.repository.CrudRepository;


@Repository
public interface GenreRepository extends CrudRepository<Genre, Long>{
}
