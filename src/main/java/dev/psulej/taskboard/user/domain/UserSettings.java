package dev.psulej.taskboard.user.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collation = "user-settings")
public record UserSettings(
        UUID id,
        String theme
) {
}
