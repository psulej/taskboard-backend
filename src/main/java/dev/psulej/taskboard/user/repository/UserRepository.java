package dev.psulej.taskboard.user.repository;
import dev.psulej.taskboard.user.domain.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, UUID> {

    Optional<UserEntity> findByLoginIgnoreCase(String login);

    Optional<UserEntity> findByEmailIgnoreCase(String email);

    @Query("{ _id:  {$nin:  ?0}}")
    List<UserEntity> findByIdNotIn(Iterable<UUID> users);

    @Query("{ $and: [{_id:  {$nin:  ?0} }, { login: { $regex: ?1, $options: 'i'}} ]}")
    List<UserEntity> findByIdNotInAAndLoginStartsWith(Iterable<UUID> users, String loginPhraseRegex);
}
