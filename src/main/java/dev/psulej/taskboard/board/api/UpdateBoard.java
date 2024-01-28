package dev.psulej.taskboard.board.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateBoard(
        @NotBlank
        @Size(min=1, max=100, message = "Board name edit is valid")
        String name,
        List<UpdateUser> boardUpdatedUsers
) {
}

