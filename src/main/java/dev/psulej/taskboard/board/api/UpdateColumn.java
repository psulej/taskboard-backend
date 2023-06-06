package dev.psulej.taskboard.board.api;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateColumn(
        @NotBlank
        @Size(min=1, max=17, message = "Column title edit is valid")
        String title
) {
}
