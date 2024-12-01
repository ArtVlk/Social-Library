package ru.gwolk.librarysocial.AppBackend.CRUDRepositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.gwolk.librarysocial.AppBackend.Entities.Subscription;
import ru.gwolk.librarysocial.AppBackend.Entities.User;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface SubscriptionsRepository extends CrudRepository<Subscription, Long> {
    @Query("SELECT s FROM Subscription s WHERE s.user = :user")
    List<Subscription> findSubscriptionsByUser(User user);

    @Query("SELECT s FROM Subscription s WHERE s.subscribedUser = :subscribedUser")
    List<Subscription> findSubscriptionsBySubscribedUser(User subscribedUser);

    @Query("SELECT s.subscribedUser FROM Subscription s")
    List<User> findAllSubscribedUsers();

    Subscription findSubscriptionByUserAndSubscribedUser(User user, User subscribedUser);
}
