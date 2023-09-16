package dev.psulej.taskboard.board.controller;

import dev.psulej.taskboard.board.api.Column;
import dev.psulej.taskboard.board.api.CreateColumn;
import dev.psulej.taskboard.board.api.UpdateColumn;
import dev.psulej.taskboard.board.api.UpdateColumnTasks;
import dev.psulej.taskboard.board.service.ColumnService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class ColumnController {

    private final ColumnService columnService;

    @PostMapping("/{boardId}/columns")
    public Column addColumn(
            @PathVariable UUID boardId,
            @Valid @RequestBody CreateColumn createColumn
    ) {
        return columnService.addColumn(boardId, createColumn);
    }

    @PutMapping("/{boardId}/columns/{columnId}")
    public Column editColumn(
            @PathVariable UUID boardId,
            @PathVariable UUID columnId,
            @Valid @RequestBody UpdateColumn updateColumn
    ) {
        return columnService.editColumn(boardId, columnId, updateColumn);
    }

    @DeleteMapping("/{boardId}/columns/{columnId}")
    public void deleteColumn(
            @PathVariable UUID boardId,
            @PathVariable UUID columnId
    ) {
        columnService.deleteColumn(boardId, columnId);
    }

    @PutMapping("/{boardId}/columns")
    public void updateColumns(
            @PathVariable UUID boardId,
            @RequestBody List<UpdateColumnTasks> columns
    ) {
        columnService.updateColumns(boardId, columns);
    }
}
