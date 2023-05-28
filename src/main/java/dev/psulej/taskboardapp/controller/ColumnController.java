package dev.psulej.taskboardapp.controller;

import dev.psulej.taskboardapp.api.CreateColumn;
import dev.psulej.taskboardapp.api.UpdateColumn;
import dev.psulej.taskboardapp.api.UpdateColumnTasks;
import dev.psulej.taskboardapp.model.Column;
import dev.psulej.taskboardapp.service.ColumnService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/boards")
public class ColumnController {

    ColumnService columnService;

    public ColumnController(ColumnService columnService) {
        this.columnService = columnService;
    }

    @PostMapping("/{boardId}/columns")
    public Column addColumn(
            @PathVariable UUID boardId,
            @RequestBody CreateColumn createColumn
    ) {
        return columnService.addColumn(boardId, createColumn);
    }

    @PutMapping("/{boardId}/columns/{columnId}")
    public Column editColumn(
            @PathVariable UUID boardId,
            @PathVariable UUID columnId,
            @RequestBody UpdateColumn updateColumn
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
