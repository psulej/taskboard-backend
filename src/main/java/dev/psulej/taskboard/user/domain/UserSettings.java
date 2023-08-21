package dev.psulej.taskboard.user.domain;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.UUID;

@Document(collection = "user-settings")
@Builder
public record UserSettings(
        UUID id,
        String theme,
        String avatarColor
) {
}
