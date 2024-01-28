package dev.psulej.taskboard.board.repository;
import dev.psulej.taskboard.board.domain.ColumnEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ColumnRepository extends MongoRepository<ColumnEntity, UUID> {
}
