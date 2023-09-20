package dev.psulej.taskboard.board.api;
import dev.psulej.taskboard.user.api.User;
import lombok.Builder;


import java.util.UUID;

@Builder
public record Task(
        UUID id,
        String title,
        String description,
        User assignedUser
) {
}
