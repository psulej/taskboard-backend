package dev.psulej.taskboardapp.repository;
import dev.psulej.taskboardapp.model.Column;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.UUID;
public interface ColumnRepository extends MongoRepository<Column, UUID> {
}
