package dev.psulej.taskboardapp.board.repository;

import dev.psulej.taskboardapp.board.domain.Board;
import dev.psulej.taskboardapp.user.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BoardRepository extends MongoRepository<Board, UUID> {
    List<User> findUsersById(UUID boardId);
}
