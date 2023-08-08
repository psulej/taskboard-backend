package dev.psulej.taskboard.image.repository;

import dev.psulej.taskboard.image.domain.Image;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface ImageRepository extends MongoRepository<Image, UUID> {
}
