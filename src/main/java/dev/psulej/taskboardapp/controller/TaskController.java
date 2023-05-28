package dev.psulej.taskboardapp.controller;

import dev.psulej.taskboardapp.api.CreateTask;
import dev.psulej.taskboardapp.api.UpdateTask;
import dev.psulej.taskboardapp.model.Task;
import dev.psulej.taskboardapp.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/boards")
public class TaskController {
    TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/{boardId}/columns/{columnId}/tasks")
    public Task addTask(
            @PathVariable UUID boardId,
            @PathVariable UUID columnId,
            @RequestBody CreateTask createTask
    ) {
        return taskService.addTask(boardId, columnId, createTask);
    }

    @PutMapping("/{boardId}/columns/{columnId}/tasks/{taskId}")
    public Task editTask(
            @PathVariable UUID boardId,
            @PathVariable UUID columnId,
            @PathVariable UUID taskId,
            @RequestBody UpdateTask updateTask
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
