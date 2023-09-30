package dev.psulej.taskboard.board.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BoardNotFoundException extends IllegalArgumentException{
    public BoardNotFoundException(String s) {
        super(s);
    }
}
