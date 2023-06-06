package dev.psulej.taskboard.board.service;

import dev.psulej.taskboard.board.api.AvailableBoard;
import dev.psulej.taskboard.board.api.CreateBoard;
import dev.psulej.taskboard.board.api.UpdateBoard;
import dev.psulej.taskboard.board.domain.Board;
import dev.psulej.taskboard.board.repository.BoardRepository;
import dev.psulej.taskboard.board.repository.ColumnRepository;
import dev.psulej.taskboard.board.repository.TaskRepository;
import dev.psulej.taskboard.user.domain.User;
import dev.psulej.taskboard.user.repository.UserRepository;
import dev.psulej.taskboard.user.service.UserService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Service
public class BoardService {
    private final MongoTemplate mongoTemplate;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public BoardService(MongoTemplate mongoTemplate, BoardRepository boardRepository, ColumnRepository columnRepository, TaskRepository taskRepository, UserRepository userRepository, UserService userService) {
        this.mongoTemplate = mongoTemplate;
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public List<AvailableBoard> getAvailableBoards() {
        User loggedUser = userService.getLoggedUser();
        Query query = new Query();
        Criteria criteria = Criteria.where("users.$id").in(loggedUser.id());
        query.addCriteria(criteria);
        return mongoTemplate.find(query, AvailableBoard.class, "boards");
    }

    public Board getBoard(UUID boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board was not found"));
    }

    public Board addBoard(CreateBoard createBoard) {
        User loggedUser = userService.getLoggedUser();
        Board board = Board.builder()
                .id(UUID.randomUUID())
                .name(createBoard.name())
                .users(List.of(loggedUser))
                .columns(List.of())
                .build();
        return boardRepository.save(board);
    }

    public Board editBoard(UUID boardId, UpdateBoard updateBoard) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("Board not found"));
        List<UUID> updatedBoardIds = updateBoard.userIds();
        List<User> updatedBoardUsers = userRepository.findAllById(updatedBoardIds);

        User loggedUser = userService.getLoggedUser();
        if(!updatedBoardUsers.contains(loggedUser)) {
            throw new IllegalArgumentException("Logged in user can not be deleted!");
        }

        Board newBoard = Board.builder()
                .id(boardId)
                .name(updateBoard.name())
                .users(updatedBoardUsers)
                .columns(board.columns())
                .build();
        return boardRepository.save(newBoard);
    }

    public void deleteBoard(UUID boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("Board not found"));
        boardRepository.deleteById(boardId);
    }

    public List<User> getAssignableUsers(UUID boardId, String loginPhrase, List<UUID> excludedUserIds) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));
        List<UUID> boardUserIds = board.users().stream().map(User::id).toList();

        Set<UUID> allExcludedUserIds = Stream.of(boardUserIds, excludedUserIds)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toUnmodifiableSet());

        if (StringUtils.hasText(loginPhrase)) {
            String loginPhraseRegex = "^" + loginPhrase + ".*";
            return userRepository.findByIdNotInAAndLoginStartsWith(allExcludedUserIds, loginPhraseRegex);
        } else {
            return userRepository.findByIdNotIn(allExcludedUserIds);
        }
    }
}
