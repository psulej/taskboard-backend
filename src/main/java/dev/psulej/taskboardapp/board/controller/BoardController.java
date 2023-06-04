package dev.psulej.taskboardapp.board.controller;

import dev.psulej.taskboardapp.board.api.AvailableBoard;
import dev.psulej.taskboardapp.board.api.CreateBoard;
import dev.psulej.taskboardapp.board.api.UpdateBoard;
import dev.psulej.taskboardapp.board.domain.Board;
import dev.psulej.taskboardapp.board.service.BoardService;
import dev.psulej.taskboardapp.user.domain.User;
import jakarta.validation.Valid;
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