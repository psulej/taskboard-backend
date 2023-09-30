package dev.psulej.taskboard.comment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CommentNotFoundException extends IllegalArgumentException{
    public CommentNotFoundException(String s) {
        super(s);
    }
}
