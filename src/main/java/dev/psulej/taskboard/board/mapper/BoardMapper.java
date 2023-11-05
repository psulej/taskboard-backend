package dev.psulej.taskboard.board.mapper;
import dev.psulej.taskboard.board.api.Board;
import dev.psulej.taskboard.board.api.BoardUser;
import dev.psulej.taskboard.board.api.Column;

import dev.psulej.taskboard.board.api.User;
import dev.psulej.taskboard.board.domain.BoardEntity;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BoardMapper {

    private final ColumnMapper columnMapper;
    private final UserMapper userMapper;

    public Board mapBoard(BoardEntity board) {

        return Board.builder()
                .id(board.id())
                .name(board.name())
                .users(mapUsers(board))
                .columns(mapColumns(board))
                .build();
    }

    private List<BoardUser> mapUsers(BoardEntity board) {
        return CollectionUtils.emptyIfNull(board.users()).stream()
                .map(boardUser -> BoardUser.builder()
                        .user(userMapper.mapUser(boardUser.user()))
                        .role(boardUser.role())
                        .joinedAt(boardUser.joinedAt())
                        .build())
                .toList();
    }

    private List<Column> mapColumns(BoardEntity board) {
        return CollectionUtils.emptyIfNull(board.columns()).stream()
                .map(columnMapper::mapColumn)
                .toList();
    }

}
