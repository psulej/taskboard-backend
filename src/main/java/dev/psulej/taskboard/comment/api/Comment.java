package dev.psulej.taskboard.comment.api;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record Comment(
        UUID id,
        String description,
        CommentUser user,
        Instant createdAt,
        Instant updatedAt
) {
}
