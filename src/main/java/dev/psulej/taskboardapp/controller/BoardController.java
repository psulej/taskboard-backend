package dev.psulej.taskboardapp.controller;

import dev.psulej.taskboardapp.api.*;
import dev.psulej.taskboardapp.model.Board;
import dev.psulej.taskboardapp.model.Column;
import dev.psulej.taskboardapp.model.Task;
import dev.psulej.taskboardapp.model.User;
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

    @GetMapping("/{boardId}")
    public Board getBoard(@PathVariable UUID boardId) {
        return boardService.getBoard(boardId);
    }

    @PostMapping
    public Board addBoard(
            @RequestBody CreateBoard createBoard) {
        return boardService.addBoard(createBoard);
    }

    @PutMapping("/{boardId}")
    public Board editBoard(
            @PathVariable UUID boardId,
            @RequestBody UpdateBoard updateBoard
    ) {
        return boardService.editBoard(boardId, updateBoard);
    }

    @DeleteMapping("/{boardId}")
    public void deleteBoard(
            @PathVariable UUID boardId
    ) {
        boardService.deleteBoard(boardId);
    }

    @GetMapping("/{boardId}/assignable-users")
    public List<User> getAssignableUsers(
            @PathVariable UUID boardId,
            @RequestParam(required = false) String loginPhrase
    ) {
        return boardService.getAssignableUsers(boardId, loginPhrase);
    }

    @PostMapping("/{boardId}/columns")
    public Column addColumn(
            @PathVariable UUID boardId,
            @RequestBody CreateColumn createColumn
    ) {
        return boardService.addColumn(boardId, createColumn);
    }

    @PutMapping("/{boardId}/columns/{columnId}")
    public Column editColumn(
            @PathVariable UUID boardId,
            @PathVariable UUID columnId,
            @RequestBody UpdateColumn updateColumn
    ) {
        return boardService.editColumn(boardId, columnId, updateColumn);
    }

    @DeleteMapping("/{boardId}/columns/{columnId}")
    public void deleteColumn(
            @PathVariable UUID boardId,
            @PathVariable UUID columnId
    ) {
        boardService.deleteColumn(boardId, columnId);
    }

    @PutMapping("/{boardId}/columns")
    public void updateColumns(
            @PathVariable UUID boardId,
            @RequestBody List<UpdateColumnTasks> columns
    ) {
        boardService.updateColumns(boardId, columns);
    }

    @PostMapping("/{boardId}/columns/{columnId}/tasks")
    public Task addTask(
            @PathVariable UUID boardId,
            @PathVariable UUID columnId,
            @RequestBody CreateTask createTask
    ) {
        return boardService.addTask(boardId, columnId, createTask);
    }

    @PutMapping("/{boardId}/columns/{columnId}/tasks/{taskId}")
    public Task editTask(
            @PathVariable UUID boardId,
            @PathVariable UUID columnId,
            @PathVariable UUID taskId,
            @RequestBody UpdateTask updateTask
    ) {
        return boardService.editTask(boardId, columnId, taskId, updateTask);
    }

    @DeleteMapping("{boardId}/columns/{columnId}/tasks/{taskId}")
    public void deleteTask(
            @PathVariable UUID boardId,
            @PathVariable UUID columnId,
            @PathVariable UUID taskId
    ) {
        boardService.deleteTask(boardId, columnId, taskId);
    }

}