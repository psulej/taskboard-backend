package dev.psulej.taskboard.comment.domain;
import dev.psulej.taskboard.user.domain.UserEntity;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.Instant;
import java.util.UUID;

@Document("comments")
@Builder
public record CommentEntity(

        @Id
        UUID id,

        @Field
        String description,

        @DBRef
        UserEntity user,

        @Field
        Instant createdAt
) {
}
