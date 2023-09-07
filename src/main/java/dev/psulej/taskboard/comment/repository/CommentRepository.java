package dev.psulej.taskboard.comment.repository;
import dev.psulej.taskboard.comment.domain.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.UUID;

public interface CommentRepository extends MongoRepository<Comment, UUID> {
}
