package dev.psulej.taskboard.comment.service;

import dev.psulej.taskboard.board.domain.TaskEntity;
import dev.psulej.taskboard.board.repository.TaskRepository;
import dev.psulej.taskboard.comment.api.NewComment;
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
    private final TaskRepository taskRepository;
    private final UserService userService;

    public void addComment(UUID taskId, NewComment newComment) {
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        UserEntity loggedUser = userService.getLoggedUser();

        CommentEntity comment = CommentEntity.builder()
                .user(loggedUser)
                .description(newComment.description())
                .id(UUID.randomUUID())
                .createdAt(Instant.now())
                .build();

        List<CommentEntity> oldCommentsList = task.comments();
        List<CommentEntity> newCommentsList;

        if (oldCommentsList != null) {
            newCommentsList = new ArrayList<>(oldCommentsList);
        } else {
            newCommentsList = new ArrayList<>();
        }

        newCommentsList.add(comment);
        commentRepository.save(comment);

        taskRepository.save(TaskEntity.builder()
                .id(task.id())
                .title(task.title())
                .description(task.description())
                .assignedUser(task.assignedUser())
                .comments(newCommentsList)
                .build()
        );
    }

    public void deleteComment(UUID commentId) {
        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        commentRepository.deleteById(commentId);
    }
}
