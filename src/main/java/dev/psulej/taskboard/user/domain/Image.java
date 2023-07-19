package dev.psulej.taskboard.user.domain;

import lombok.Builder;
import org.bson.types.Binary;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "images")
@Builder
public record Image(
        UUID id,
        String fileName,
        Binary fileContent
) {
}
