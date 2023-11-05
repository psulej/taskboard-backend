package dev.psulej.taskboard.board.api;

import lombok.Builder;

import java.util.UUID;

@Builder
public record User(
        UUID id,
        String login,
        String name,
        UUID imageId,
        String avatarColor
) {
}
