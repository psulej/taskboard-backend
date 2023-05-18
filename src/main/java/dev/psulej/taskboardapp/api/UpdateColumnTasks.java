package dev.psulej.taskboardapp.api;

import java.util.List;
import java.util.UUID;

public record UpdateColumnTasks(
        UUID columnId,
        List<UUID> taskIds
) {
}
