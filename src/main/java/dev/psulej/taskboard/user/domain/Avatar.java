package dev.psulej.taskboard.user.domain;

import lombok.Builder;
import org.bson.types.Binary;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collation = "avatars")
@Builder
public record Avatar(
        UUID id,
        String name,
        Binary avatar
) {
}
