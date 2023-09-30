package dev.psulej.taskboard.board.service;

import dev.psulej.taskboard.board.api.CreateTask;
import dev.psulej.taskboard.board.api.Task;
import dev.psulej.taskboard.board.api.UpdateTask;
import dev.psulej.taskboard.board.domain.ColumnEntity;
import dev.psulej.taskboard.board.domain.TaskEntity;
import dev.psulej.taskboard.board.exception.BoardNotFoundException;
import dev.psulej.taskboard.board.exception.ColumnNotFoundException;
import dev.psulej.taskboard.board.exception.TaskNotFoundException;
import dev.psulej.taskboard.board.mapper.TaskMapper;
import dev.psulej.taskboard.user.domain.UserEntity;
import dev.psulej.taskboard.board.repository.BoardRepository;
import dev.psulej.taskboard.board.repository.ColumnRepository;
import dev.psulej.taskboard.board.repository.TaskRepository;
import dev.psulej.taskboard.user.exception.UserNotFoundException;
import dev.psulej.taskboard.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final BoardUpdatePublisher boardUpdatePublisher;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final ColumnRepository columnRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public Task addTask(UUID boardId, UUID columnId, CreateTask createTask) {
        ColumnEntity column = columnRepository.findById(columnId).orElseThrow(()
                ->  new ColumnNotFoundException("Column with columnId " + columnId
                + " was not found in the board with id: " + boardId));
        TaskEntity newTask = TaskEntity.builder()
                .id(UUID.randomUUID())
                .title(createTask.title())
                .description(createTask.description())
                .assignedUser(null)
                .build();
        List<TaskEntity> oldTasksList = column.tasks();
        List<TaskEntity> newTaskList = new ArrayList<>(oldTasksList);
        newTaskList.add(newTask);
        taskRepository.save(newTask);
        ColumnEntity newColumn = ColumnEntity.builder()
                .id(columnId)
                .name(column.name())
                .tasks(newTaskList)
                .build();
        columnRepository.save(newColumn);
        boardUpdatePublisher.publish(boardId);
        return taskMapper.mapTask(newTask);
    }

    public void deleteTask(UUID boardId, UUID columnId, UUID taskId) {
        ColumnEntity column = columnRepository.findById(columnId).orElseThrow(()
                -> new ColumnNotFoundException("Column with columnId " + columnId
                + " was not found in the board with id: " + boardId));
        List<TaskEntity> oldTasksList = column.tasks();
        List<TaskEntity> newTaskList = oldTasksList.stream().filter(task -> task.id() != taskId).toList();
        ColumnEntity newColumn = ColumnEntity.builder()
                .id(column.id())
                .name(column.name())
                .tasks(newTaskList)
                .build();
        columnRepository.save(newColumn);
        taskRepository.deleteById(taskId);
        boardUpdatePublisher.publish(boardId);
    }

    public Task editTask(UUID boardId, UUID columnId, UUID taskId, UpdateTask updateTask) {
        if (!boardRepository.existsById(boardId)) throw new BoardNotFoundException("Board with id: " + boardId +
                " not found in the column with id: " + columnId);
        if (!columnRepository.existsById(columnId)) throw new ColumnNotFoundException("Column with columnId " + columnId
                + " was not found in the board with id: " + boardId);
        UserEntity assignedUser = Optional.ofNullable(updateTask.assignedUserId())
                .map(assignedUserId ->
                        userRepository.findById(assignedUserId)
                                .orElseThrow(() -> new UserNotFoundException("User with id: " + assignedUserId + " not found")))
                .orElse(null);
        TaskEntity taskEntity = taskRepository.findById(taskId)
                .map(task -> task.toBuilder()
                        .title(updateTask.title())
                        .description(updateTask.description())
                        .assignedUser(assignedUser)
                        .build())
                .map(taskRepository::save)
                .orElseThrow(() -> new TaskNotFoundException("Task with taskId: " + taskId + " was not found in the column with" +
                        "columnId: " + columnId));
        boardUpdatePublisher.publish(boardId);
        return taskMapper.mapTask(taskEntity);
    }
}
