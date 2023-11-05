package dev.psulej.taskboard.board.api;

import dev.psulej.taskboard.board.domain.BoardUserRole;

import java.util.UUID;

public record UpdateUser(
        UUID userId,
        BoardUserRole role
) {
}
