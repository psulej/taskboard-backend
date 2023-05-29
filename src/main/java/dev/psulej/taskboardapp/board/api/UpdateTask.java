package dev.psulej.taskboardapp.board.api;
import java.util.UUID;

public record UpdateTask(
        String title,
        String description,
        UUID assignedUserId
) { }
