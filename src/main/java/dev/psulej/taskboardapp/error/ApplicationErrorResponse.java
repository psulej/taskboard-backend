package dev.psulej.taskboardapp.error;

import java.util.List;

public record ApplicationErrorResponse(List<ApplicationError> errors) {
}
