package ru.gwolk.librarysocial.CRUDRepositories;

import org.springframework.stereotype.Repository;
import ru.gwolk.librarysocial.Entities.PhoneNumber;
import org.springframework.data.repository.CrudRepository;



@Repository
public interface PhoneNumberRepository extends CrudRepository<PhoneNumber, Long> {
}
