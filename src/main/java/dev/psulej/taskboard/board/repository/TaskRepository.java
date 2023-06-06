package dev.psulej.taskboard.board.repository;
import dev.psulej.taskboard.board.domain.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
@Repository
public interface TaskRepository extends MongoRepository<Task, UUID> {
}
