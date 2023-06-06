package dev.psulej.taskboard.error;

import java.util.List;

public record ApplicationErrorResponse(List<ApplicationError> errors) {
}
