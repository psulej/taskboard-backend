package dev.psulej.taskboardapp.board.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateBoard(
        @NotBlank
        @Size(min=1, max=17, message = "Board name insert is valid")
        String name
) {
}
