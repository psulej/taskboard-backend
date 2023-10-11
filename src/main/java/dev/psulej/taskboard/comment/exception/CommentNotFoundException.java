package dev.psulej.taskboard.comment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CommentNotFoundException extends IllegalArgumentException{
    public CommentNotFoundException(UUID commentId) {
        super("Comment with id: "+ commentId +" not found");
    }
}
