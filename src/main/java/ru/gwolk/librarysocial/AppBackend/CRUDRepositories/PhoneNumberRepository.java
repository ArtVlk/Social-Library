package ru.gwolk.librarysocial.AppBackend.CRUDRepositories;

import org.springframework.stereotype.Repository;
import ru.gwolk.librarysocial.AppBackend.Entities.PhoneNumber;
import org.springframework.data.repository.CrudRepository;



@Repository
public interface PhoneNumberRepository extends CrudRepository<PhoneNumber, Long> {
}
