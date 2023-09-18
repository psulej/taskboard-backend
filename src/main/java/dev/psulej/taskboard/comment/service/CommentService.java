package dev.psulej.taskboard.comment.service;

import dev.psulej.taskboard.board.domain.TaskEntity;
import dev.psulej.taskboard.board.mapper.CommentMapper;
import dev.psulej.taskboard.board.repository.TaskRepository;
import dev.psulej.taskboard.comment.api.Comment;
import dev.psulej.taskboard.comment.api.NewComment;
import dev.psulej.taskboard.comment.api.UpdateComment;
import dev.psulej.taskboard.comment.domain.CommentEntity;
import dev.psulej.taskboard.comment.repository.CommentRepository;
import dev.psulej.taskboard.user.domain.UserEntity;
import dev.psulej.taskboard.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final TaskRepository taskRepository;
    private final UserService userService;

    public Comment addComment(UUID taskId, NewComment newComment) {
        TaskEntity taskEntity = taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        UserEntity loggedUser = userService.getLoggedUser();

        CommentEntity comment = CommentEntity.builder()
                .user(loggedUser)
                .description(newComment.description())
                .id(UUID.randomUUID())
                .createdAt(Instant.now())
                .build();

        List<CommentEntity> oldCommentsList = taskEntity.comments();
        List<CommentEntity> newCommentsList;

        if (oldCommentsList != null) {
            newCommentsList = new ArrayList<>(oldCommentsList);
        } else {
            newCommentsList = new ArrayList<>();
        }

        newCommentsList.add(comment);
        commentRepository.save(comment);

        taskRepository.save(TaskEntity.builder()
                .id(taskEntity.id())
                .title(taskEntity.title())
                .description(taskEntity.description())
                .assignedUser(taskEntity.assignedUser())
                .comments(newCommentsList)
                .build()
        );

        return commentMapper.mapComment(comment);
    }

    public void deleteComment(UUID commentId) {
        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        commentRepository.deleteById(commentId);
    }

    public Comment editComment(UUID commentId, UpdateComment updateComment) {
        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        CommentEntity updatedComment = CommentEntity.builder()
                .user(comment.user())
                .description(updateComment.description())
                .id(comment.id())
                .createdAt(Instant.now())
                .build();

        return commentMapper.mapComment(updatedComment);
    }
}
