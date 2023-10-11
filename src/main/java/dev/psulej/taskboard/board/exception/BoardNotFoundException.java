package dev.psulej.taskboard.board.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BoardNotFoundException extends IllegalArgumentException{

    public BoardNotFoundException(UUID boardId) {
        super("Board with id: " + boardId + " not found");
    }

    public BoardNotFoundException(UUID boardId, UUID columnId) {
        super("Board with id: " + boardId +
        " not found in the column with id: " + columnId);
    }
}
