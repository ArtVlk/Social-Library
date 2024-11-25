package ru.gwolk.librarysocial.CRUDRepositories;

import org.springframework.stereotype.Repository;
import ru.gwolk.librarysocial.Entities.Genre;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


@Repository
public interface GenreRepository extends CrudRepository<Genre, Long>{
    Optional<Genre> findByName(String name);
}
