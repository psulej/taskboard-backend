package dev.psulej.taskboard.board.api;

import java.util.UUID;

public record AvailableBoard(
        UUID id,
        String name
) {
}
