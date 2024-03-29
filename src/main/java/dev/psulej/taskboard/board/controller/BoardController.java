package dev.psulej.taskboard.board.controller;

import dev.psulej.taskboard.board.api.*;
import dev.psulej.taskboard.board.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;

    @GetMapping
    public List<AvailableBoard> getBoards() {
        return boardService.getAvailableBoards();
    }

    @GetMapping("/{boardId}")
    @PreAuthorize("hasRole('USER')")
    public Board getBoard(@PathVariable UUID boardId) {
        return boardService.getBoard(boardId);
    }

    @PostMapping
    public Board addBoard(
            @Valid @RequestBody CreateBoard createBoard) {
        return boardService.addBoard(createBoard);
    }

    @PutMapping("/{boardId}")
    public Board editBoard(
            @PathVariable UUID boardId,
            @Valid @RequestBody UpdateBoard updateBoard
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
            @RequestParam(required = false) String loginPhrase,
            @RequestParam(required = false) List<UUID> excludedUserIds
    ) {
        return boardService.getAssignableUsers(boardId, loginPhrase, excludedUserIds);
    }
}