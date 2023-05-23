package dev.psulej.taskboardapp.repository;
import dev.psulej.taskboardapp.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends MongoRepository<User, UUID> {
    Optional<User> findByLogin(String login);

    @Query("{ _id:  {$nin:  ?0}}")
    List<User> findByIdNotIn(List<UUID> users);
}
