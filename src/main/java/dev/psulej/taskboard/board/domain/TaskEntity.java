package dev.psulej.taskboard.board.domain;

import dev.psulej.taskboard.comment.domain.CommentEntity;
import dev.psulej.taskboard.user.domain.UserEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.UUID;

@Document("tasks")
@Builder(toBuilder = true)
public record TaskEntity(
        @Id
        UUID id,

        @Field
        String title,

        @Field
        String description,

        @DBRef
        UserEntity assignedUser,

        @DBRef(lazy = true)
        List<CommentEntity> comments
) {

}
