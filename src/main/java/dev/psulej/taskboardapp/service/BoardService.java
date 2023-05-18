package dev.psulej.taskboardapp.service;

import dev.psulej.taskboardapp.api.*;
import dev.psulej.taskboardapp.model.Board;
import dev.psulej.taskboardapp.model.Column;
import dev.psulej.taskboardapp.model.Task;
import dev.psulej.taskboardapp.repository.BoardRepository;
import dev.psulej.taskboardapp.repository.ColumnRepository;
import dev.psulej.taskboardapp.repository.TaskRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BoardService {

    private final MongoTemplate mongoTemplate;
    private final BoardRepository boardRepository;
    private final ColumnRepository columnRepository;
    private final TaskRepository taskRepository;

    public BoardService(MongoTemplate mongoTemplate, BoardRepository boardRepository, ColumnRepository columnRepository, TaskRepository taskRepository) {
        this.mongoTemplate = mongoTemplate;
        this.boardRepository = boardRepository;
        this.columnRepository = columnRepository;
        this.taskRepository = taskRepository;
    }

    public List<AvailableBoard> getAvailableBoards(){
        Query query = new Query();
        // Wyszukiwanie boardow tylko dla usera
        // Criteria criteria = Criteria.where("users").elemMatch(Criteria.where("id").is(UUID.randomUUID()));
        Criteria criteria = new Criteria();
        query.addCriteria(criteria);
        return mongoTemplate.find(query, AvailableBoard.class, "boards");
    }

    public Board getBoard(UUID id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Board was not found"));
    }

    public void updateColumns(UUID id,List<UpdateColumnTasks> updatedColumns) {
        Board board = getBoard(id);

        Map<UUID,List<Task>> taskCount = board.columns().stream()
                .map(Column::tasks)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(
                        Task::id
                ));

        Map<UUID,Task> tasksById = board.columns().stream()
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

        updatedColumns.forEach(updatedColumn ->{
            UUID columnId = updatedColumn.columnId();
            Column column = columnsById.get(columnId);
            if (column == null) {
                throw new IllegalStateException("Column with id " + columnId + " was not found in the board " + id);
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

            Column newColumn = new Column(column.id(), column.name(), newTasks);

            columnRepository.save(newColumn);

            newColumns.add(newColumn);
        });

        Board newBoard = new Board(board.id(), board.name(), board.users(), newColumns);
        boardRepository.save(newBoard);

        System.out.println("boardId: " + id);
    }

    public Column addColumn(UUID id, CreateColumn createColumn){
        Board board = getBoard(id);
        Column column = new Column(UUID.randomUUID(),createColumn.title(),List.of());
        columnRepository.save(column);

        List<Column> oldColumns = board.columns();
        List<Column> newColumns = new ArrayList<>(oldColumns);
        newColumns.add(column);

        Board newBoard = new Board(id,board.name(), board.users(), newColumns);

        boardRepository.save(newBoard);
        return column;
    }

    public Task addTask(UUID boardId, UUID columnId, CreateTask createTask) {

        Column column = columnRepository.findById(columnId).orElseThrow(()
                -> new IllegalArgumentException("Column not found"));

        Task newTask = new Task(UUID.randomUUID(), createTask.title(), createTask.description(), null);

        List<Task> oldTasksList = column.tasks();
        List<Task> newTaskList = new ArrayList<>(oldTasksList);
        newTaskList.add(newTask);
        taskRepository.save(newTask);

        Column newColumn = new Column(columnId, column.name(), newTaskList);
        columnRepository.save(newColumn);

        return newTask;
    }

    public void deleteColumn(UUID boardId, UUID columnId) {
        Board board = getBoard(boardId);
        List<Column> oldColumnList = board.columns();
        List<Column> newColumnList = oldColumnList.stream().filter(column -> !column.id().equals(columnId)).toList();
        Board newBoard = new Board(board.id(),board.name(), board.users(), newColumnList);
        boardRepository.save(newBoard);
        columnRepository.deleteById(columnId);
    }

    public void deleteTask(UUID boardId, UUID columnId, UUID taskId) {

        Column column = columnRepository.findById(columnId).orElseThrow(()
                -> new IllegalArgumentException("Column not found"));

        List<Task> oldTasksList = column.tasks();
        List<Task> newTaskList = oldTasksList.stream().filter(task -> task.id() != taskId).toList();

        Column newColumn = new Column(column.id(), column.name(), newTaskList);

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

        return taskRepository.findById(taskId)
                .map(task -> new Task(task.id(), updateTask.title(), updateTask.description(), null))
                .map(taskRepository::save)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
    }

    public Column editColumn(UUID boardId, UUID columnId, UpdateColumn updateColumn) {

        if (!boardRepository.existsById(boardId)) {
            throw new IllegalArgumentException("Board not found");
        }

        return columnRepository.findById(columnId)
                .map(column -> new Column(column.id(), updateColumn.title(), column.tasks()))
                .map(columnRepository::save)
                .orElseThrow(() -> new IllegalArgumentException("Column not found"));
    }

}
