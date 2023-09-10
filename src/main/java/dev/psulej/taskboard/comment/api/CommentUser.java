package dev.psulej.taskboard.comment.api;

import java.util.UUID;

public record CommentUser(
        String login,
        String name,
        UUID imageId,
        String avatarColor
) {
}
