package dev.psulej.taskboard.comment.repository;
import dev.psulej.taskboard.comment.domain.CommentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.UUID;

public interface CommentRepository extends MongoRepository<CommentEntity, UUID> {
}
