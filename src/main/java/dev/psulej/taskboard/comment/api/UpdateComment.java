package dev.psulej.taskboard.comment.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateComment(
        @NotBlank
        @Size(min=1, max=1000, message = "Comment description update is valid")
        String description
) {
}
