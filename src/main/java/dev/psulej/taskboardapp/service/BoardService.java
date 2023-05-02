package dev.psulej.taskboardapp.service;

import dev.psulej.taskboardapp.api.AvailableBoard;
import dev.psulej.taskboardapp.api.CreateColumn;
import dev.psulej.taskboardapp.api.CreateTask;
import dev.psulej.taskboardapp.api.UpdateColumn;
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

    public void updateColumns(UUID id,List<UpdateColumn> updatedColumns) {
        Board board = getBoard(id);

        Map<UUID,Task> tasksById = board.getColumns().stream()
                .map(Column::getTasks)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(
                        Task::getId,
                        Function.identity()
                ));

        Map<UUID, Column> columnsById = board.getColumns().stream().collect(Collectors.toMap(
                Column::getId,
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

            Column newColumn = new Column(column.getId(), column.getName(), newTasks);

            columnRepository.save(newColumn);

            newColumns.add(newColumn);
        });

        Board newBoard = new Board(board.getId(), board.getName(), board.getUsers(), newColumns);
        boardRepository.save(newBoard);

        System.out.println("boardId: " + id);
    }

    public Column addColumn(UUID id, CreateColumn createColumn){
        Board board = getBoard(id);
        Column column = new Column(UUID.randomUUID(),createColumn.title(),List.of());
        columnRepository.save(column);

        List<Column> oldColumns = board.getColumns();
        List<Column> newColumns = new ArrayList<>(oldColumns);
        newColumns.add(column);

        Board newBoard = new Board(id,board.getName(), board.getUsers(), newColumns);

        boardRepository.save(newBoard);
        return column;
    }

    public Task addTask(UUID boardId, UUID columnId, CreateTask createTask) {

        Column column = columnRepository.findById(columnId).orElseThrow(()
                -> new IllegalArgumentException("Column not found"));

        Task newTask = new Task(UUID.randomUUID(), createTask.title(), createTask.description());

        List<Task> oldTasksList = column.getTasks();
        List<Task> newTaskList = new ArrayList<>(oldTasksList);
        newTaskList.add(newTask);
        taskRepository.save(newTask);

        Column newColumn = new Column(columnId, column.getName(), newTaskList);
        columnRepository.save(newColumn);

        return newTask;
    }
}
