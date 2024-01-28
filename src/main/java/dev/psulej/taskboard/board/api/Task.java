package dev.psulej.taskboard.board.api;

import dev.psulej.taskboard.board.domain.TaskPriority;
import lombok.Builder;

import java.util.UUID;

@Builder
public record Task(
        UUID id,
        String title,
        String description,
        User assignedUser,
        TaskPriority priority
) {
}
