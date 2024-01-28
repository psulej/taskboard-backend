package dev.psulej.taskboard.board.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ColumnNotFoundException extends IllegalArgumentException{

    public ColumnNotFoundException(UUID boardId, UUID columnId) {
        super("Column with columnId " + columnId + " was not found in the board with id: " + boardId);
    }
}
