package dev.psulej.taskboard.user.domain;

import dev.psulej.taskboard.user.api.ApplicationTheme;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "user-settings")
@Builder
public record UserSettings(
        UUID id,
        ApplicationTheme theme
) {
}
