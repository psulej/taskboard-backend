package dev.psulej.taskboard.board.api;

import dev.psulej.taskboard.user.api.UserView;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record Board(
        UUID id,
        String name,
        List<UserView> users,
        List<Column> columns
) {
}

