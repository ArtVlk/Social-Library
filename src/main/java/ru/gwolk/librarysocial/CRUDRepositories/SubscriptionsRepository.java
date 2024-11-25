package ru.gwolk.librarysocial.CRUDRepositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.gwolk.librarysocial.Entities.Subscription;
import org.springframework.data.jpa.repository.Query;
import ru.gwolk.librarysocial.Entities.User;

import java.util.List;

@Repository
public interface SubscriptionsRepository extends CrudRepository<Subscription, Long> {
    @Query("SELECT s FROM Subscription s WHERE s.user = :user")
    List<Subscription> findSubscriptionsByUser(User user);

    @Query("SELECT s.subscribedUser FROM Subscription s")
    List<User> findAllSubscribedUsers();

    Subscription findSubscriptionByUserAndSubscribedUser(User user, User subscribedUser);
}
