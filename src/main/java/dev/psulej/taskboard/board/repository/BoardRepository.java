package dev.psulej.taskboard.board.repository;
import dev.psulej.taskboard.board.domain.BoardEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BoardRepository extends MongoRepository<BoardEntity, UUID> {
}
