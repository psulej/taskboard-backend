package dev.psulej.taskboard.board.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TaskNotFoundException extends IllegalArgumentException{
    public TaskNotFoundException(UUID columnId, UUID taskId) {
        super("Task with id " + taskId + " was not found in the column with id: " + columnId);
    }
    public TaskNotFoundException(UUID taskId) {
        super("Task with taskId: " + taskId + " not found");
    }
}
