package dev.psulej.taskboardapp.controller;

import dev.psulej.taskboardapp.api.AvailableBoard;
import dev.psulej.taskboardapp.api.CreateColumn;
import dev.psulej.taskboardapp.api.UpdateColumn;
import dev.psulej.taskboardapp.model.Board;
import dev.psulej.taskboardapp.model.Column;
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

//    @DeleteMapping("/{id}/columns")
//    public Column deleteColumn(){}

    @PutMapping("/{id}/columns")
    public void updateColumns(
            @PathVariable UUID id,
            @RequestBody List<UpdateColumn> columns
            ){
        boardService.updateColumns(id,columns);
    }

}