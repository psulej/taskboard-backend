package dev.psulej.taskboard.board.api;

import dev.psulej.taskboard.board.domain.BoardUserRole;
import lombok.Builder;

import java.time.Instant;

@Builder
public record BoardUser(
        User user,
        BoardUserRole role,
        Instant joinedAt
) {
}
