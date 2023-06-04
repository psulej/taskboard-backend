package dev.psulej.taskboardapp.board.domain;

import dev.psulej.taskboardapp.user.domain.User;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Document("tasks")
@Builder
public record Task(
        @Id
        UUID id,

        @Field
        String title,

        @Field
        String description,

        @DBRef
        User assignedUser
) {
}
