package crud.repository;

import entity.Country;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CountryRepository extends CrudRepository<Country, Long>{
}
