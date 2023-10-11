package dev.psulej.taskboard.board.service;

import dev.psulej.taskboard.board.api.Board;
import dev.psulej.taskboard.board.domain.BoardEntity;
import dev.psulej.taskboard.board.exception.BoardNotFoundException;
import dev.psulej.taskboard.board.mapper.BoardMapper;
import dev.psulej.taskboard.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardUpdatePublisher {

    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void publish(UUID boardId) {
        Board board = boardRepository.findById(boardId)
                .map(boardMapper::mapBoard)
                .orElseThrow(() -> new BoardNotFoundException(boardId));
        this.publish(board);
    }

    public void publish(BoardEntity board) {
        this.publish(boardMapper.mapBoard(board));
    }

    public void publish(Board board) {
        simpMessagingTemplate.convertAndSend("/topic/boards/%s".formatted(board.id()), board);
    }
}
