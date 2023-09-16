package dev.psulej.taskboard.board.service;

import dev.psulej.taskboard.board.api.*;
import dev.psulej.taskboard.board.domain.BoardEntity;
import dev.psulej.taskboard.board.domain.TaskEntity;
import dev.psulej.taskboard.board.mapper.BoardMapper;
import dev.psulej.taskboard.board.repository.BoardRepository;
import dev.psulej.taskboard.board.repository.TaskRepository;
import dev.psulej.taskboard.user.domain.UserEntity;
import dev.psulej.taskboard.user.repository.UserRepository;
import dev.psulej.taskboard.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final MongoTemplate mongoTemplate;
    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final UserService userService;


    public List<AvailableBoard> getAvailableBoards() {
        UserEntity loggedUser = userService.getLoggedUser();
        Query query = new Query();
        Criteria criteria = Criteria.where("users.$id").in(loggedUser.id());
        query.addCriteria(criteria);
        return mongoTemplate.find(query, AvailableBoard.class, "boards");
    }

    public Board getBoard(UUID boardId) {
        return boardRepository.findById(boardId)
                .map(boardMapper::mapBoard)
                .orElseThrow(() -> new IllegalArgumentException("Board was not found"));
    }

    public Board addBoard(CreateBoard createBoard) {
        UserEntity loggedUser = userService.getLoggedUser();
        BoardEntity boardEntity = boardRepository.save(BoardEntity.builder()
                .id(UUID.randomUUID())
                .name(createBoard.name())
                .users(List.of(loggedUser))
                .columns(List.of())
                .build());

        return boardMapper.mapBoard(boardEntity);
    }

    public Board editBoard(UUID boardId, UpdateBoard updateBoard) {
        BoardEntity board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("Board not found"));
        List<UUID> updatedBoardIds = updateBoard.userIds();
        List<UserEntity> updatedBoardUsers = userRepository.findAllById(updatedBoardIds);

        UserEntity loggedUser = userService.getLoggedUser();
        if (!updatedBoardUsers.contains(loggedUser)) {
            throw new IllegalArgumentException("Logged in user can not be deleted!");
        }

        List<UUID> boardUsersIds = updateBoard.userIds();
        List<TaskEntity> tasksToUpdate = board.columns().stream()
                .flatMap(column -> column.tasks().stream())
                .filter(task -> task.assignedUser() != null)
                .filter(task -> !boardUsersIds.contains(task.assignedUser().id()))
                .map(task -> task.toBuilder().assignedUser(null).build())
                .toList();
        taskRepository.saveAll(tasksToUpdate);

        BoardEntity boardEntity = boardRepository.save(BoardEntity.builder()
                .id(boardId)
                .name(updateBoard.name())
                .users(updatedBoardUsers)
                .columns(board.columns())
                .build());

        return boardMapper.mapBoard(boardEntity);
    }

    public void deleteBoard(UUID boardId) {
        BoardEntity board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("Board not found"));
        boardRepository.deleteById(boardId);
    }

    public List<UserEntity> getAssignableUsers(UUID boardId, String loginPhrase, List<UUID> excludedUserIds) {
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));
        List<UUID> boardUserIds = board.users().stream().map(UserEntity::id).toList();

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
