package dev.psulej.taskboard.board.domain;

import dev.psulej.taskboard.user.domain.UserEntity;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.UUID;

@Document("boards")
@Builder
public record BoardEntity(
        @Id
        UUID id,

        @Field
        String name,

        @DBRef
        List<UserEntity> users,

        @DBRef
        List<ColumnEntity> columns
) {
}
