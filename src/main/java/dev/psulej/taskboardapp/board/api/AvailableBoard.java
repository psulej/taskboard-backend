package dev.psulej.taskboardapp.board.api;

import java.util.UUID;

public record AvailableBoard(
        UUID id,
        String name
) {
}
