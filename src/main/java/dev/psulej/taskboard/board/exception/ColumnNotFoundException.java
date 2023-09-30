package dev.psulej.taskboard.board.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ColumnNotFoundException extends IllegalArgumentException{
    public ColumnNotFoundException(String s) {
        super(s);
    }
}
