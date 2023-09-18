package dev.psulej.taskboard.board.mapper;


import dev.psulej.taskboard.comment.api.Comment;
import dev.psulej.taskboard.comment.domain.CommentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    public Comment mapComment(CommentEntity comment) {
        return Comment.builder()
                .id(comment.id())
                .createdAt(comment.createdAt())
                .description(comment.description())
                .user(comment.user())
                .build();
    }
}
