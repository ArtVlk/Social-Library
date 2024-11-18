package ru.gwolk.librarysocial.CRUDRepositories;

import org.springframework.stereotype.Repository;
import ru.gwolk.librarysocial.Entities.Contact;
import ru.gwolk.librarysocial.Entities.Note;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;



import java.util.List;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Long> {
    List<Contact> findByName(String name);
    List<Contact> findByGender(String gender);

    @Query("SELECT c FROM Contact c WHERE c.country.name = :country")
    List<Contact> findByCountry(@Param("country") String country);

    @Query("SELECT c FROM Contact c WHERE c.note = :note")
    List<Contact> findByNote(@Param("note") Note note);
}
