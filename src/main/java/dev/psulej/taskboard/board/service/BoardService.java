package dev.psulej.taskboard.board.service;

import dev.psulej.taskboard.board.api.*;
import dev.psulej.taskboard.board.domain.BoardEntity;
import dev.psulej.taskboard.board.domain.BoardUser;
import dev.psulej.taskboard.board.domain.BoardUserRole;
import dev.psulej.taskboard.board.domain.TaskEntity;
import dev.psulej.taskboard.board.exception.BoardNotFoundException;
import dev.psulej.taskboard.board.mapper.BoardMapper;
import dev.psulej.taskboard.board.mapper.UserMapper;
import dev.psulej.taskboard.board.repository.BoardRepository;
import dev.psulej.taskboard.board.repository.TaskRepository;
import dev.psulej.taskboard.board.api.User;
import dev.psulej.taskboard.user.domain.UserEntity;
import dev.psulej.taskboard.user.exception.UserHasNoPermissionException;
import dev.psulej.taskboard.user.repository.UserRepository;
import dev.psulej.taskboard.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
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
    private final UserMapper userMapper;
    private final BoardUpdatePublisher boardUpdatePublisher;
    private final BoardPermissionValidator boardPermissionValidator;


    public List<AvailableBoard> getAvailableBoards() {
        UserEntity loggedUser = userService.getLoggedUser();
        Query query = new Query();
        Criteria criteria = Criteria.where("users.user.$id").in(loggedUser.id());
        query.addCriteria(criteria);
        return mongoTemplate.find(query, AvailableBoard.class, "boards");
    }

    public Board getBoard(UUID boardId) {
        BoardEntity boardEntity = boardRepository.findById(boardId).orElseThrow(() -> new BoardNotFoundException(boardId));
        return boardMapper.mapBoard(boardEntity);
    }

    public Board addBoard(CreateBoard createBoard) {
        UserEntity loggedUser = userService.getLoggedUser();

        BoardUser boardUser = BoardUser.builder()
                .user(loggedUser)
                .joinedAt(Instant.now())
                .role(BoardUserRole.BOARD_ADMINISTRATOR)
                .build();

        BoardEntity boardEntity = boardRepository.save(BoardEntity.builder()
                .id(UUID.randomUUID())
                .name(createBoard.name())
                .users(List.of(boardUser))
                .columns(List.of())
                .build());

        boardUpdatePublisher.publish(boardEntity);
        return boardMapper.mapBoard(boardEntity);
    }

    public Board editBoard(UUID boardId, UpdateBoard updateBoard) {
        boardPermissionValidator.validateBoardModifying(boardId);

        BoardEntity board = boardRepository.findById(boardId).orElseThrow(() -> new BoardNotFoundException(boardId));
        List<UUID> updatedBoardUsersIds = updateBoard.boardUpdatedUsers()
                .stream()
                .map(UpdateUser::userId)
                .toList();

        List<UserEntity> userEntities = userRepository.findAllById(updatedBoardUsersIds);
        Map<UUID, UserEntity> usersByUserId = userRepository.findAllById(updatedBoardUsersIds).stream()
                .collect(Collectors.toMap(UserEntity::id, Function.identity()));

        List<BoardUser> updatedBoardUsers = updateBoard.boardUpdatedUsers()
                .stream()
                .map(boardUser -> BoardUser.builder()
                        .user(usersByUserId.get(boardUser.userId()))
                        .role(boardUser.role())
                        .joinedAt(Instant.now())
                        .build()
                )
                .toList();

        UserEntity loggedUser = userService.getLoggedUser();
        if (!userEntities.contains(loggedUser)) {
            throw new UserHasNoPermissionException("Logged user with id: " + loggedUser.id() + " cannot be deleted");
        }

        List<TaskEntity> tasksToUpdate = board.columns().stream()
                .flatMap(column -> column.tasks().stream())
                .filter(task -> task.assignedUser() != null)
                .filter(task -> !updatedBoardUsersIds.contains(task.assignedUser().id()))
                .map(task -> task.toBuilder().assignedUser(null).build())
                .toList();
        taskRepository.saveAll(tasksToUpdate);

        BoardEntity boardEntity = boardRepository.save(BoardEntity.builder()
                .id(boardId)
                .name(updateBoard.name())
                .users(updatedBoardUsers)
                .columns(board.columns())
                .build());


        boardUpdatePublisher.publish(boardEntity);
        return boardMapper.mapBoard(boardEntity);
    }

    public void deleteBoard(UUID boardId) {
        boardPermissionValidator.validateBoardModifying(boardId);
        BoardEntity board = boardRepository.findById(boardId).orElseThrow(() -> new BoardNotFoundException(boardId));
        boardRepository.deleteById(boardId);
        boardUpdatePublisher.publish(board);
    }

    public List<User> getAssignableUsers(UUID boardId, String loginPhrase, List<UUID> excludedUserIds) {
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException(boardId));

        List<UUID> boardUserIds = board.users().
                stream()
                .map(boardUser -> boardUser.user().id())
                .toList();

        Set<UUID> allExcludedUserIds = Stream.of(boardUserIds, excludedUserIds)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toUnmodifiableSet());

        if (StringUtils.hasText(loginPhrase)) {
            String loginPhraseRegex = "^" + loginPhrase + ".*";
            List<UserEntity> usersByIdNotInAAndLoginStartsWith = userRepository.findByIdNotInAAndLoginStartsWith(allExcludedUserIds, loginPhraseRegex);
            return getUsersIds(usersByIdNotInAAndLoginStartsWith);
        } else {
            List<UserEntity> usersByIdNotIn = userRepository.findByIdNotIn(allExcludedUserIds);
            return getUsersIds(usersByIdNotIn);
        }
    }

    private List<User> getUsersIds(List<UserEntity> usersIds) {
        return usersIds.stream().map(userMapper::mapUser).toList();
    }
}
