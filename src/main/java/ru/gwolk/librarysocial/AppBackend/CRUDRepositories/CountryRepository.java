package ru.gwolk.librarysocial.AppBackend.CRUDRepositories;

import org.springframework.stereotype.Repository;
import ru.gwolk.librarysocial.AppBackend.Entities.Country;
import org.springframework.data.repository.CrudRepository;
import ru.gwolk.librarysocial.AppBackend.Entities.Subscription;

/**
 * Репозиторий для работы с сущностью {@link Country}.
 */
@Repository
public interface CountryRepository extends CrudRepository<Country, Long>{
}
