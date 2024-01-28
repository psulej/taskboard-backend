package dev.psulej.taskboard.board.domain;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.UUID;

@Document("columns")
@Builder(toBuilder = true)
public record ColumnEntity(
        @Id
        UUID id,

        @Field
        String name,

        @DBRef
        List<TaskEntity> tasks
) {
}

