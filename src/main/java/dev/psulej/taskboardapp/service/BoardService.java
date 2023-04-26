package dev.psulej.taskboardapp.service;

import dev.psulej.taskboardapp.api.AvailableBoard;
import dev.psulej.taskboardapp.api.UpdateColumn;
import dev.psulej.taskboardapp.model.Board;
import dev.psulej.taskboardapp.model.Column;
import dev.psulej.taskboardapp.model.Task;
import dev.psulej.taskboardapp.repository.BoardRepository;
import dev.psulej.taskboardapp.repository.ColumnRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public BoardService(MongoTemplate mongoTemplate, BoardRepository boardRepository) {
        this.mongoTemplate = mongoTemplate;
        this.boardRepository = boardRepository;
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

    @Autowired
    ColumnRepository columnRepository;

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
}
