package dev.psulej.taskboard.comment.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateComment(
        @NotBlank
        @Size(min=1, max=1000, message = "Comment description insert is valid")
        String description
) {
}
