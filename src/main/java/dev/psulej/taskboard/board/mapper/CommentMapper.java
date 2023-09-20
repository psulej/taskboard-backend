package dev.psulej.taskboard.board.mapper;


import dev.psulej.taskboard.comment.api.Comment;
import dev.psulej.taskboard.comment.domain.CommentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final UserMapper userMapper;

    public Comment mapComment(CommentEntity comment) {
        return Comment.builder()
                .id(comment.id())
                .createdAt(comment.createdAt())
                .updatedAt(comment.updatedAt())
                .description(comment.description())
                .user(userMapper.mapUser(comment.user()))
                .build();
    }
}
