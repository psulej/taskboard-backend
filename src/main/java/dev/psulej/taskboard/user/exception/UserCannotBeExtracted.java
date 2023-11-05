package dev.psulej.taskboard.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UserCannotBeExtracted extends IllegalArgumentException{
    public UserCannotBeExtracted(String message) {
        super(message);
    }
}
