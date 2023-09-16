package dev.psulej.taskboard.board.mapper;
import dev.psulej.taskboard.board.api.Board;
import dev.psulej.taskboard.board.api.Column;

import dev.psulej.taskboard.board.domain.BoardEntity;
import dev.psulej.taskboard.user.api.UserView;
import dev.psulej.taskboard.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BoardMapper {

    private final ColumnMapper columnMapper;

    public Board mapBoard(BoardEntity board) {

        return Board.builder()
                .id(board.id())
                .name(board.name())
                .users(CollectionUtils.emptyIfNull(board.users()).stream()
                        .map(this::mapUser)
                        .toList())
                .columns(mapColumns(board))
                .build();
    }

    private List<Column> mapColumns(BoardEntity board) {
        return CollectionUtils.emptyIfNull(board.columns()).stream()
                .map(columnMapper::mapColumn)
                .toList();
    }



    private UserView mapUser(UserEntity user) {
        return UserView.builder()
                .id(user.id())
                .login(user.login())
                .name(user.name())
                .imageId(user.imageId())
                .avatarColor(user.avatarColor())
                .build();
    }
}
