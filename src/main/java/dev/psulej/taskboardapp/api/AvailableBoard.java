package dev.psulej.taskboardapp.api;

import java.util.UUID;

public record AvailableBoard(
        UUID id,
        String name
) {
}
