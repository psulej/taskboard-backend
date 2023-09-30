package dev.psulej.taskboard.board.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record UpdateTask(
        @NotBlank
        @Size(min=1, max=500, message = "Task title edit is valid")
        String title,

        @Size(max = 10000, message = "Task description too long")
        String description,

        UUID assignedUserId
) { }
