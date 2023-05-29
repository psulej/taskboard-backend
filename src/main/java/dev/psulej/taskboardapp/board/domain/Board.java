package dev.psulej.taskboardapp.board.domain;

import dev.psulej.taskboardapp.user.domain.User;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.List;
import java.util.UUID;

@Document("boards")
@Builder
public record Board(
        @Id UUID id,
        @Field String name,
        @DBRef List<User> users,
        @DBRef List<Column> columns
) {
}
