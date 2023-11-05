package dev.psulej.taskboard.board.api;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record Board(
        UUID id,
        String name,
        List<BoardUser> users,
        List<Column> columns
) {
}

