package crud.repository;

import entity.Genre;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "genres")
public interface GenreRepository extends CrudRepository<Genre, Long>{
}
