package dev.psulej.taskboardapp.board.api;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record UpdateBoard(
        @NotBlank
        @Size(min=1, max=17, message = "Board name edit is valid")
        String name,

        List<UUID> userIds
) {
}
