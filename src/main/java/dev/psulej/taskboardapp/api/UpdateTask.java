package dev.psulej.taskboardapp.api;
import java.util.UUID;

public record UpdateTask(
        String title,
        String description,
        UUID assignedUserId
) { }
