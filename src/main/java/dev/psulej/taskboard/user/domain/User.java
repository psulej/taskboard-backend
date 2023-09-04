package dev.psulej.taskboard.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.UUID;

@Document("users")
@Builder(toBuilder = true)
public record  User(
        @Id UUID id,

        @Field String login,

        @Field String email,

        @Field String name,

        @Field UserRole role,

        @Field
        @JsonIgnore
        String password,

        @Field UUID imageId,

        @Field String avatarColor
) {
}

