package dev.psulej.taskboardapp.service;

import dev.psulej.taskboardapp.api.CreateTask;
import dev.psulej.taskboardapp.api.UpdateTask;
import dev.psulej.taskboardapp.model.Column;
import dev.psulej.taskboardapp.model.Task;
import dev.psulej.taskboardapp.model.User;
import dev.psulej.taskboardapp.repository.BoardRepository;
import dev.psulej.taskboardapp.repository.ColumnRepository;
import dev.psulej.taskboardapp.repository.TaskRepository;
import dev.psulej.taskboardapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final ColumnRepository columnRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, ColumnRepository columnRepository, BoardRepository boardRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.columnRepository = columnRepository;
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
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
}
