package ru.gwolk.librarysocial.CRUDRepositories;

import org.springframework.stereotype.Repository;
import ru.gwolk.librarysocial.Entities.User;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
}
