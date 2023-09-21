package dev.psulej.taskboard.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserHasNoPermissionException extends IllegalArgumentException {

    public UserHasNoPermissionException(String message) {
        super(message);
    }
}
