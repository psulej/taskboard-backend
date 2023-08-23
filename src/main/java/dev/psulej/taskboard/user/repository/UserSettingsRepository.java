package dev.psulej.taskboard.user.repository;
import dev.psulej.taskboard.user.domain.UserSettings;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserSettingsRepository extends MongoRepository<UserSettings, UUID> {
    Optional<UserSettings> findByUserId(UUID userId);
}

