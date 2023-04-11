package dev.psulej.taskboardapp.controller;

import dev.psulej.taskboardapp.model.Board;
import dev.psulej.taskboardapp.service.BoardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boards")
public class BoardController {

    BoardService boardService;

    @GetMapping
    public Board getBoard(){
        return boardService.getFirstBoard();
    }
}
