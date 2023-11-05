package dev.psulej.taskboard.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserSettingsNotFound extends IllegalArgumentException{
    public UserSettingsNotFound(String message) {
        super(message);
    }
}
