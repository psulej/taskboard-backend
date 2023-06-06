package dev.psulej.taskboard.board.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateColumn(
        @NotBlank
        @Size(min=1, max=17, message = "Column title insert is valid")
        String title
) {
}
