package crud.repository;

import entity.PhoneNumber;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource
public interface PhoneNumberRepository extends CrudRepository<PhoneNumber, Long> {
}
