package dev.psulej.taskboard.user.domain;
import dev.psulej.taskboard.user.api.ApplicationTheme;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Document(collection = "user-settings")
@Builder(toBuilder = true)
public record UserSettingsEntity(
        @Id UUID userId,
        @Field ApplicationTheme applicationTheme,
        @Field String avatarColor
) {
}
