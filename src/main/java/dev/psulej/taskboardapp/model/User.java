package dev.psulej.taskboardapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.UUID;

@Document("users")
@Builder
public record User(
        @Id UUID id,
        @Field String login,
        @Field @JsonIgnore String password
) {
}

