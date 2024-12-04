package ru.gwolk.librarysocial.AppBackend.CRUDRepositories;

import org.springframework.stereotype.Repository;
import ru.gwolk.librarysocial.AppBackend.Entities.Address;
import org.springframework.data.repository.CrudRepository;
import ru.gwolk.librarysocial.AppBackend.Entities.Subscription;


/**
 * Репозиторий для работы с сущностью {@link Address}.
 */
@Repository
public interface AddressRepository extends CrudRepository<Address, Long> {
}
