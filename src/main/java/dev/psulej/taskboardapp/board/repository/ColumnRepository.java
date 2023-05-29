package dev.psulej.taskboardapp.board.repository;
import dev.psulej.taskboardapp.board.domain.Column;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.UUID;
public interface ColumnRepository extends MongoRepository<Column, UUID> {
}
