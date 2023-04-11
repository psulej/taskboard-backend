package dev.psulej.taskboardapp.service;
import dev.psulej.taskboardapp.api.CreateTask;
import dev.psulej.taskboardapp.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // POST /boards/{boardId}/columns/{columnId}/tasks
    void addTask(UUID boardId, UUID columnId, CreateTask task){

    }
}
