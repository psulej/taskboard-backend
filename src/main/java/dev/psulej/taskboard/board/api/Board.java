package dev.psulej.taskboard.board.api;

import dev.psulej.taskboard.user.api.User;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record Board(
        UUID id,
        String name,
        List<User> users,
        List<Column> columns
) {
}

