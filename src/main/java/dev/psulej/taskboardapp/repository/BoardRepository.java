package dev.psulej.taskboardapp.repository;
import dev.psulej.taskboardapp.model.Board;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface BoardRepository extends MongoRepository<Board, UUID> {
}
