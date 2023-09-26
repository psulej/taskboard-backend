package dev.psulej.taskboard.comment.service;

import dev.psulej.taskboard.board.domain.TaskEntity;
import dev.psulej.taskboard.board.mapper.CommentMapper;
import dev.psulej.taskboard.board.repository.TaskRepository;
import dev.psulej.taskboard.comment.api.Comment;
import dev.psulej.taskboard.comment.api.CreateComment;
import dev.psulej.taskboard.comment.api.UpdateComment;
import dev.psulej.taskboard.comment.domain.CommentEntity;
import dev.psulej.taskboard.comment.repository.CommentRepository;
import dev.psulej.taskboard.user.domain.UserEntity;
import dev.psulej.taskboard.user.exception.UserHasNoPermissionException;
import dev.psulej.taskboard.user.service.UserService;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
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
    private final CommentCreatedPublisher commentCreatedPublisher;

    public Comment addComment(UUID taskId, CreateComment newComment) {
        TaskEntity taskEntity = taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        UserEntity loggedUser = userService.getLoggedUser();

        CommentEntity commentEntity = CommentEntity.builder()
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

        newCommentsList.add(commentEntity);
        commentRepository.save(commentEntity);

        taskRepository.save(TaskEntity.builder()
                .id(taskEntity.id())
                .title(taskEntity.title())
                .description(taskEntity.description())
                .assignedUser(taskEntity.assignedUser())
                .comments(newCommentsList)
                .build()
        );

        Comment comment = commentMapper.mapComment(commentEntity);
        commentCreatedPublisher.publish(taskId, comment);
        return comment;
    }

    private CommentEntity checkUserCommentPermission(UUID commentId) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment with id: "+ commentId +" not found"));
        UUID loggedUserId = userService.getLoggedUserId();
        UUID commentUserId = comment.user().id();
        if(!loggedUserId.equals(commentUserId)) {
            throw new UserHasNoPermissionException("User has not permission to edit comment with id: " + commentId);
        }
        return comment;
    }

    public void deleteComment(UUID commentId) {
        CommentEntity comment = checkUserCommentPermission(commentId);
        commentRepository.deleteById(commentId);
    }

    public Comment editComment(UUID commentId, UpdateComment updateComment) {
        CommentEntity comment = checkUserCommentPermission(commentId);
        CommentEntity updatedComment = comment.toBuilder()
                .description(updateComment.description())
                .updatedAt(Instant.now())
                .build();
        return commentMapper.mapComment(commentRepository.save(updatedComment));
    }

    public List<Comment> getComments(UUID taskId) {
        TaskEntity taskEntity = taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        List<CommentEntity> taskComments = (List<CommentEntity>) CollectionUtils.emptyIfNull(taskEntity.comments());
        return taskComments.stream().map(commentMapper::mapComment).toList();
    }
}
