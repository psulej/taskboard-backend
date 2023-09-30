package dev.psulej.taskboard.board.mapper;
import dev.psulej.taskboard.comment.api.Comment;
import dev.psulej.taskboard.comment.api.CommentUser;
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
                .updatedAt(comment.updatedAt())
                .description(comment.description())
                .user(CommentUser.builder()
                        .id(comment.user().id())
                        .avatarColor(comment.user().avatarColor())
                        .imageId(comment.user().imageId())
                        .login(comment.user().login())
                        .name(comment.user().name())
                        .build())
                .build();
    }
}
