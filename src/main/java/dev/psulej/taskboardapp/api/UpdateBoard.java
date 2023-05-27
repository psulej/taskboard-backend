package dev.psulej.taskboardapp.api;

import java.util.List;
import java.util.UUID;

public record UpdateBoard(
        String name,
        List<UUID> userIds
) {
}
