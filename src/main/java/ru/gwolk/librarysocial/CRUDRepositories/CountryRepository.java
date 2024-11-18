package ru.gwolk.librarysocial.CRUDRepositories;

import org.springframework.stereotype.Repository;
import ru.gwolk.librarysocial.Entities.Country;
import org.springframework.data.repository.CrudRepository;


@Repository
public interface CountryRepository extends CrudRepository<Country, Long>{
}
