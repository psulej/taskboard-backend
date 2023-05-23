package dev.psulej.taskboardapp.repository;
import dev.psulej.taskboardapp.model.Board;
import dev.psulej.taskboardapp.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BoardRepository extends MongoRepository<Board, UUID> {

    List<User> findUsersById(UUID boardId);
}
