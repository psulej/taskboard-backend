package dev.psulej.taskboardapp.service;

import dev.psulej.taskboardapp.api.*;
import dev.psulej.taskboardapp.model.Board;
import dev.psulej.taskboardapp.model.Column;
import dev.psulej.taskboardapp.model.Task;
import dev.psulej.taskboardapp.model.User;
import dev.psulej.taskboardapp.repository.BoardRepository;
import dev.psulej.taskboardapp.repository.ColumnRepository;
import dev.psulej.taskboardapp.repository.TaskRepository;
import dev.psulej.taskboardapp.repository.UserRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BoardService {

    private final MongoTemplate mongoTemplate;
    private final BoardRepository boardRepository;
    private final ColumnRepository columnRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public BoardService(MongoTemplate mongoTemplate, BoardRepository boardRepository, ColumnRepository columnRepository, TaskRepository taskRepository, UserRepository userRepository, UserService userService) {
        this.mongoTemplate = mongoTemplate;
        this.boardRepository = boardRepository;
        this.columnRepository = columnRepository;
        this.taskRepository = taskRepository;
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

        List<User> boardUsers = board.users();

        Board newBoard = Board.builder()
                .id(boardId)
                .name(updateBoard.name())
                .users(boardUsers)
                .columns(board.columns())
                .build();
        return boardRepository.save(newBoard);
    }

    public void deleteBoard(UUID boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("Board not found"));
        boardRepository.deleteById(boardId);
    }

    public void updateColumns(UUID boardId, List<UpdateColumnTasks> updatedColumns) {
        Board board = getBoard(boardId);
        Map<UUID, List<Task>> taskCount = board.columns().stream()
                .map(Column::tasks)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(
                        Task::id
                ));
        Map<UUID, Task> tasksById = board.columns().stream()
                .map(Column::tasks)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(
                        Task::id,
                        Function.identity()
                ));
        Map<UUID, Column> columnsById = board.columns().stream().collect(Collectors.toMap(
                Column::id,
                Function.identity()
        ));
        List<Column> newColumns = new ArrayList<>();
        updatedColumns.forEach(updatedColumn -> {
            UUID columnId = updatedColumn.columnId();
            Column column = columnsById.get(columnId);
            if (column == null) {
                throw new IllegalStateException("Column with columnId " + columnId + " was not found in the board " + boardId);
            }
            List<Task> newTasks = new ArrayList<>();
            updatedColumn.taskIds().forEach(taskId -> {
                // pobranie nowego taska
                Task task = tasksById.get(taskId);
                if (task == null) {
                    throw new IllegalStateException("Task with id " + taskId + " was not found in the column " + columnId);
                }
                newTasks.add(task);
            });

            Column newColumn = Column.builder()
                    .id(column.id())
                    .name(column.name())
                    .tasks(newTasks)
                    .build();

            columnRepository.save(newColumn);

            newColumns.add(newColumn);
        });

        Board newBoard = Board.builder()
                .id(board.id())
                .name(board.name())
                .users(board.users())
                .columns(newColumns)
                .build();

        boardRepository.save(newBoard);
        System.out.println("boardId: " + boardId);
    }

    public Column addColumn(UUID boardId, CreateColumn createColumn) {
        Board board = getBoard(boardId);
        Column column = Column.builder()
                .id(UUID.randomUUID())
                .name(createColumn.title())
                .tasks(List.of())
                .build();
        columnRepository.save(column);
        List<Column> oldColumns = board.columns();
        List<Column> newColumns = new ArrayList<>(oldColumns);
        newColumns.add(column);
        Board newBoard = Board.builder()
                .id(boardId)
                .name(board.name())
                .users(board.users())
                .columns(newColumns)
                .build();
        boardRepository.save(newBoard);
        return column;
    }

    public Task addTask(UUID boardId, UUID columnId, CreateTask createTask) {
        Column column = columnRepository.findById(columnId).orElseThrow(()
                -> new IllegalArgumentException("Column not found"));
        Task newTask = Task.builder()
                .id(UUID.randomUUID())
                .title(createTask.title())
                .description(createTask.description())
                .assignedUser(null)
                .build();
        List<Task> oldTasksList = column.tasks();
        List<Task> newTaskList = new ArrayList<>(oldTasksList);
        newTaskList.add(newTask);
        taskRepository.save(newTask);
        Column newColumn = Column.builder()
                .id(columnId)
                .name(column.name())
                .tasks(newTaskList)
                .build();
        columnRepository.save(newColumn);
        return newTask;
    }

    public void deleteColumn(UUID boardId, UUID columnId) {
        Board board = getBoard(boardId);
        List<Column> oldColumnList = board.columns();
        List<Column> newColumnList = oldColumnList.stream().filter(column -> !column.id().equals(columnId)).toList();
        Board newBoard = Board.builder()
                .id(board.id())
                .name(board.name())
                .users(board.users())
                .columns(newColumnList)
                .build();
        boardRepository.save(newBoard);
        columnRepository.deleteById(columnId);
    }

    public void deleteTask(UUID boardId, UUID columnId, UUID taskId) {
        Column column = columnRepository.findById(columnId).orElseThrow(()
                -> new IllegalArgumentException("Column not found"));
        List<Task> oldTasksList = column.tasks();
        List<Task> newTaskList = oldTasksList.stream().filter(task -> task.id() != taskId).toList();
        Column newColumn = Column.builder()
                .id(column.id())
                .name(column.name())
                .tasks(newTaskList)
                .build();
        columnRepository.save(newColumn);
        taskRepository.deleteById(taskId);
    }

    public Task editTask(UUID boardId, UUID columnId, UUID taskId, UpdateTask updateTask) {
        if (!boardRepository.existsById(boardId)) {
            throw new IllegalArgumentException("Board not found");
        }
        if (!columnRepository.existsById(columnId)) {
            throw new IllegalArgumentException("Column not found");
        }
        User assignedUser = Optional.ofNullable(updateTask.assignedUserId())
                .map(assignedUserId ->
                        userRepository.findById(assignedUserId)
                                .orElseThrow(() -> new IllegalArgumentException("User not found")))
                .orElse(null);
        return taskRepository.findById(taskId)
                .map(task -> Task.builder()
                        .id(task.id())
                        .title(updateTask.title())
                        .description(updateTask.description())
                        .assignedUser(assignedUser)
                        .build())
                .map(taskRepository::save)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
    }

    public Column editColumn(UUID boardId, UUID columnId, UpdateColumn updateColumn) {
        if (!boardRepository.existsById(boardId)) {
            throw new IllegalArgumentException("Board not found");
        }
        return columnRepository.findById(columnId)
                .map(column -> Column.builder()
                        .id(column.id())
                        .name(updateColumn.title())
                        .tasks(column.tasks())
                        .build())
                .map(columnRepository::save)
                .orElseThrow(() -> new IllegalArgumentException("Column not found"));
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
