package dev.psulej.taskboard.board.service;

import dev.psulej.taskboard.board.api.Column;
import dev.psulej.taskboard.board.api.CreateColumn;
import dev.psulej.taskboard.board.api.UpdateColumn;
import dev.psulej.taskboard.board.api.UpdateColumnTasks;
import dev.psulej.taskboard.board.domain.BoardEntity;
import dev.psulej.taskboard.board.domain.ColumnEntity;
import dev.psulej.taskboard.board.domain.TaskEntity;
import dev.psulej.taskboard.board.mapper.ColumnMapper;
import dev.psulej.taskboard.board.repository.BoardRepository;
import dev.psulej.taskboard.board.repository.ColumnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ColumnService {
    private final ColumnRepository columnRepository;
    private final BoardRepository boardRepository;
    private final ColumnMapper columnMapper;

    public void updateColumns(UUID boardId, List<UpdateColumnTasks> updatedColumns) {
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board was not found"));
        Map<UUID, TaskEntity> tasksById = board.columns().stream()
                .map(ColumnEntity::tasks)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(
                        TaskEntity::id,
                        Function.identity()
                ));
        Map<UUID, ColumnEntity> columnsById = board.columns().stream().collect(Collectors.toMap(
                ColumnEntity::id,
                Function.identity()
        ));
        List<ColumnEntity> newColumns = new ArrayList<>();
        updatedColumns.forEach(updatedColumn -> {
            UUID columnId = updatedColumn.columnId();
            ColumnEntity column = columnsById.get(columnId);
            if (column == null) {
                throw new IllegalStateException("Column with columnId " + columnId + " was not found in the board " + boardId);
            }
            List<TaskEntity> newTasks = new ArrayList<>();
            updatedColumn.taskIds().forEach(taskId -> {
                // pobranie nowego taska
                TaskEntity task = tasksById.get(taskId);
                if (task == null) {
                    throw new IllegalStateException("Task with id " + taskId + " was not found in the column " + columnId);
                }
                newTasks.add(task);
            });

            ColumnEntity newColumn = ColumnEntity.builder()
                    .id(column.id())
                    .name(column.name())
                    .tasks(newTasks)
                    .build();

            columnRepository.save(newColumn);
            newColumns.add(newColumn);
        });

        BoardEntity newBoard = BoardEntity.builder()
                .id(board.id())
                .name(board.name())
                .users(board.users())
                .columns(newColumns)
                .build();

        boardRepository.save(newBoard);
        System.out.println("boardId: " + boardId);
    }

    public Column addColumn(UUID boardId, CreateColumn createColumn) {
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board was not found"));
        ColumnEntity column = ColumnEntity.builder()
                .id(UUID.randomUUID())
                .name(createColumn.title())
                .tasks(List.of())
                .build();
        columnRepository.save(column);
        List<ColumnEntity> oldColumns = board.columns();
        List<ColumnEntity> newColumns = new ArrayList<>(oldColumns);
        newColumns.add(column);
        boardRepository.save(BoardEntity.builder()
                .id(boardId)
                .name(board.name())
                .users(board.users())
                .columns(newColumns)
                .build());
        return columnMapper.mapColumn(column);
    }

    public Column editColumn(UUID boardId, UUID columnId, UpdateColumn updateColumn) {
        if (!boardRepository.existsById(boardId)) throw new IllegalArgumentException("Board not found");
        ColumnEntity columnEntity = columnRepository.findById(columnId)
                .map(column -> ColumnEntity.builder()
                        .id(column.id())
                        .name(updateColumn.title())
                        .tasks(column.tasks())
                        .build())
                .map(columnRepository::save)
                .orElseThrow(() -> new IllegalArgumentException("Column not found"));
        return columnMapper.mapColumn(columnEntity);
    }

    public void deleteColumn(UUID boardId, UUID columnId) {
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board was not found"));
        List<ColumnEntity> oldColumnList = board.columns();
        List<ColumnEntity> newColumnList = oldColumnList.stream().filter(column -> !column.id().equals(columnId)).toList();
        BoardEntity newBoard = BoardEntity.builder()
                .id(board.id())
                .name(board.name())
                .users(board.users())
                .columns(newColumnList)
                .build();
        boardRepository.save(newBoard);
        columnRepository.deleteById(columnId);
    }
}
