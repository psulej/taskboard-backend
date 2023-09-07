package dev.psulej.taskboard.comment.domain;

import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.UUID;

@Document("comments")
@Builder
public record Comment(

        @Id
        UUID id,

        @Field
        String description,

        @Field
        UUID userId,

        @Field
        Instant createdAt
) {
}
