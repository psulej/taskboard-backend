package dev.psulej.taskboard.board.repository;
import dev.psulej.taskboard.board.domain.ColumnEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.UUID;
public interface ColumnRepository extends MongoRepository<ColumnEntity, UUID> {
}
