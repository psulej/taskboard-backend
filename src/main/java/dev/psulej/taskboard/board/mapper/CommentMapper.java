package dev.psulej.taskboard.board.mapper;

import dev.psulej.taskboard.board.domain.TaskEntity;
import dev.psulej.taskboard.comment.api.Comment;
import dev.psulej.taskboard.comment.domain.CommentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final UserMapper userMapper;

    public Comment mapComment(TaskEntity task, CommentEntity comment) {
        return Comment.builder()
                .id(comment.id())
                .createdAt(comment.createdAt())
                .description(comment.description())
                .user(userMapper.mapUser(task.assignedUser()))
                .build();
    }
}
