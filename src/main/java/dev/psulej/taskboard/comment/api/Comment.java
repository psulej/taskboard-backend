package dev.psulej.taskboard.comment.api;
import dev.psulej.taskboard.user.api.UserView;
import lombok.Builder;
import java.time.Instant;
import java.util.UUID;

@Builder
public record Comment(
        UUID id,
        String description,
        UserView user,
        Instant createdAt
) {
}
