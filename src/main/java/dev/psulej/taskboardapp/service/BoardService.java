package dev.psulej.taskboardapp.service;

import dev.psulej.taskboardapp.model.Board;
import dev.psulej.taskboardapp.repository.BoardRepository;
import org.springframework.stereotype.Service;

@Service
public class BoardService {
    BoardRepository boardRepository;
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }
    public Board getFirstBoard(){
        return boardRepository.findAll().stream().findFirst().orElse(null);
    }

//     testowa metoda
//    public void addBoard( {} )
}
