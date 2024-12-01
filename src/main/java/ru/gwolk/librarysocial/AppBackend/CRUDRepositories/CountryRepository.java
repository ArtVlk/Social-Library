package ru.gwolk.librarysocial.AppBackend.CRUDRepositories;

import org.springframework.stereotype.Repository;
import ru.gwolk.librarysocial.AppBackend.Entities.Country;
import org.springframework.data.repository.CrudRepository;


@Repository
public interface CountryRepository extends CrudRepository<Country, Long>{
}
