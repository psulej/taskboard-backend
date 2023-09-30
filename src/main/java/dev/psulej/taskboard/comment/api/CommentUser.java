package dev.psulej.taskboard.comment.api;

import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record CommentUser(
        UUID id,
        String login,
        String name,
        UUID imageId,
        String avatarColor
) {
}
