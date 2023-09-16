package dev.psulej.taskboard.board.controller;
import dev.psulej.taskboard.board.api.CreateTask;
import dev.psulej.taskboard.board.api.Task;
import dev.psulej.taskboard.board.api.UpdateTask;
import dev.psulej.taskboard.board.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/{boardId}/columns/{columnId}/tasks")
    public Task addTask(
            @PathVariable UUID boardId,
            @PathVariable UUID columnId,
            @Valid @RequestBody CreateTask createTask
    ) {
        return taskService.addTask(boardId, columnId, createTask);
    }

    @PutMapping("/{boardId}/columns/{columnId}/tasks/{taskId}")
    public Task editTask(
            @PathVariable UUID boardId,
            @PathVariable UUID columnId,
            @PathVariable UUID taskId,
            @Valid @RequestBody UpdateTask updateTask
    ) {
        return taskService.editTask(boardId, columnId, taskId, updateTask);
    }

    @DeleteMapping("{boardId}/columns/{columnId}/tasks/{taskId}")
    public void deleteTask(
            @PathVariable UUID boardId,
            @PathVariable UUID columnId,
            @PathVariable UUID taskId
    ) {
        taskService.deleteTask(boardId, columnId, taskId);
    }
}
