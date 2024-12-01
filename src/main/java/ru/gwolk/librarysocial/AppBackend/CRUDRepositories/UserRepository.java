package ru.gwolk.librarysocial.AppBackend.CRUDRepositories;

import org.springframework.stereotype.Repository;
import ru.gwolk.librarysocial.AppBackend.Entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;



import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findByName(String name);
    List<User> findByGender(String gender);

    @Query("SELECT c FROM User c WHERE c.country.name = :country")
    List<User> findByCountry(@Param("country") String country);

}
