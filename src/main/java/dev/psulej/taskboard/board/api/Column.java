package dev.psulej.taskboard.board.api;
import lombok.Builder;


import java.util.List;
import java.util.UUID;

@Builder
public record Column(
        UUID id,
        String name,
        List<Task> tasks
) {
}
