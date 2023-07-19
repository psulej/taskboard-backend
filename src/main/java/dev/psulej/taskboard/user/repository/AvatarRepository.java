package dev.psulej.taskboard.user.repository;

import dev.psulej.taskboard.user.domain.Image;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface AvatarRepository extends MongoRepository<Image, UUID> {
}
