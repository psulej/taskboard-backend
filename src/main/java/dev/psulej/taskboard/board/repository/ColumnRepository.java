package dev.psulej.taskboard.board.repository;
import dev.psulej.taskboard.board.domain.Column;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.UUID;
public interface ColumnRepository extends MongoRepository<Column, UUID> {
}
