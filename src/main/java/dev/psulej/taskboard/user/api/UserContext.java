package dev.psulej.taskboard.user.api;
import lombok.Builder;
import java.util.UUID;

@Builder
public record UserContext(
        UUID id,
        String login,
        String email,
        String name,
        UUID imageId,
        ApplicationTheme theme
) {
}
