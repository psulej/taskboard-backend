package dev.psulej.taskboardapp.controller;

import dev.psulej.taskboardapp.api.*;
import dev.psulej.taskboardapp.model.Board;
import dev.psulej.taskboardapp.model.Column;
import dev.psulej.taskboardapp.model.Task;
import dev.psulej.taskboardapp.service.BoardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/boards")
public class BoardController {

    BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping
    public List<AvailableBoard> getBoards() {
        return boardService.getAvailableBoards();
    }

    @GetMapping("/{id}")
    public Board getBoard(@PathVariable UUID id) {
        return boardService.getBoard(id);
    }

    @PostMapping("/{id}/columns")
    public Column addColumn(
            @PathVariable UUID id,
            @RequestBody CreateColumn createColumn
    ) {
        return boardService.addColumn(id, createColumn);
    }

    @DeleteMapping("/{boardId}/columns/{columnId}")
    public void deleteColumn(
            @PathVariable UUID boardId,
            @PathVariable UUID columnId
    ){
        boardService.deleteColumn(boardId, columnId);
    }

    @PutMapping("/{id}/columns")
    public void updateColumns(
            @PathVariable UUID id,
            @RequestBody List<UpdateColumnTasks> columns
            ){
        boardService.updateColumns(id,columns);
    }

    @PostMapping("/{boardId}/columns/{columnId}/tasks")
    public Task addTask(
            @PathVariable UUID boardId,
            @PathVariable UUID columnId,
            @RequestBody CreateTask createTask
            ){
        return boardService.addTask(boardId,columnId,createTask);
    }

    @PutMapping("/{boardId}/columns/{columnId}/tasks/{taskId}")
    public Task editTask(
            @PathVariable UUID boardId,
            @PathVariable UUID columnId,
            @PathVariable UUID taskId,
            @RequestBody UpdateTask updateTask
    ){
        return boardService.editTask(boardId,columnId, taskId, updateTask);
    }

    @PutMapping("/{boardId}/columns/{columnId}")
    public Column editColumn(
            @PathVariable UUID boardId,
            @PathVariable UUID columnId,
            @RequestBody UpdateColumn updateColumn
    ) {
        return boardService.editColumn(boardId, columnId, updateColumn);
    }

    @DeleteMapping("{boardId}/columns/{columnId}/tasks/{taskId}")
    public void deleteTask(
            @PathVariable UUID boardId,
            @PathVariable UUID columnId,
            @PathVariable UUID taskId
            ){
        boardService.deleteTask(boardId,columnId,taskId);
    }

}