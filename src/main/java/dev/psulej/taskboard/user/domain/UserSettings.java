package dev.psulej.taskboard.user.domain;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Document(collection = "user-settings")
@Builder(toBuilder = true)
public record UserSettings(
        @Id UUID userId,
        @Field String theme,
        @Field String avatarColor
) {
}
