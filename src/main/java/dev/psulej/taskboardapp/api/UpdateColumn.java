package dev.psulej.taskboardapp.api;

import java.util.List;
import java.util.UUID;

public record UpdateColumn(
        UUID columnId,
        List<UUID> taskIds
) {
}
