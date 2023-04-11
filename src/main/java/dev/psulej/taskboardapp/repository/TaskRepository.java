package dev.psulej.taskboardapp.repository;
import dev.psulej.taskboardapp.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface TaskRepository extends MongoRepository<Task, UUID> {
}
