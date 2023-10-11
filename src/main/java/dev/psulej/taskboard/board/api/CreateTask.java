package dev.psulej.taskboard.board.api;

import dev.psulej.taskboard.board.domain.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTask(
        @NotBlank
        @Size(min=1, max=500, message = "Task title insert is valid")
        String title,

        @Size(max = 10000, message = "Task description too long")
        String description,

        TaskPriority priority
) { }
