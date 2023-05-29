package dev.psulej.taskboardapp.board.service;

import dev.psulej.taskboardapp.board.api.CreateColumn;
import dev.psulej.taskboardapp.board.api.UpdateColumn;
import dev.psulej.taskboardapp.board.api.UpdateColumnTasks;
import dev.psulej.taskboardapp.board.domain.Board;
import dev.psulej.taskboardapp.board.domain.Column;
import dev.psulej.taskboardapp.board.domain.Task;
import dev.psulej.taskboardapp.board.repository.BoardRepository;
import dev.psulej.taskboardapp.board.repository.ColumnRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ColumnService {
    ColumnRepository columnRepository;
    BoardRepository boardRepository;

    public ColumnService(ColumnRepository columnRepository, BoardRepository boardRepository) {
        this.columnRepository = columnRepository;
        this.boardRepository = boardRepository;
    }


    public void updateColumns(UUID boardId, List<UpdateColumnTasks> updatedColumns) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board was not found"));
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
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board was not found"));
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

    public void deleteColumn(UUID boardId, UUID columnId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board was not found"));
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


}
