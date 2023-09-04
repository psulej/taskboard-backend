package dev.psulej.taskboard.board.domain;

import dev.psulej.taskboard.user.domain.User;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Document("tasks")
@Builder(toBuilder = true)
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
