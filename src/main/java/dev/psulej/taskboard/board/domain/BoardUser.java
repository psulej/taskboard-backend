package dev.psulej.taskboard.board.domain;

import dev.psulej.taskboard.user.domain.UserEntity;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.Instant;

@Builder(toBuilder = true)
public record BoardUser(
        @DBRef
        UserEntity user,
        BoardUserRole role,
        Instant joinedAt
) {
}