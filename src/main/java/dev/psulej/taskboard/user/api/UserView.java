package dev.psulej.taskboard.user.api;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserView(
        UUID id,
        String login,
        String name,
        UUID imageId,
        String avatarColor
) {
}
