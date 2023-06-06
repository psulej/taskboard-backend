package dev.psulej.taskboard.user.repository;
import dev.psulej.taskboard.user.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface UserRepository extends MongoRepository<User, UUID> {

    Optional<User> findByLoginIgnoreCase(String login);

    Optional<User> findByEmailIgnoreCase(String email);

    @Query("{ _id:  {$nin:  ?0}}")
    List<User> findByIdNotIn(Iterable<UUID> users);

    @Query("{ $and: [{_id:  {$nin:  ?0} }, { login: { $regex: ?1, $options: 'i'}} ]}")
    List<User> findByIdNotInAAndLoginStartsWith(Iterable<UUID> users, String loginPhraseRegex);

}
