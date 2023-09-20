package dev.psulej.taskboard.comment.api;
import dev.psulej.taskboard.user.api.User;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record Comment(
        UUID id,
        String description,
        User user,
        Instant createdAt,
        Instant updatedAt
) {
}
