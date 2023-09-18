package dev.psulej.taskboard.comment.controller;
import dev.psulej.taskboard.comment.api.Comment;
import dev.psulej.taskboard.comment.api.NewComment;
import dev.psulej.taskboard.comment.api.UpdateComment;
import dev.psulej.taskboard.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{taskId}")
    public Comment addComment(@PathVariable UUID taskId, @Valid @RequestBody NewComment newComment) {
        return commentService.addComment(taskId, newComment);
    }

    @DeleteMapping("{commentId}")
    public void deleteComment(@PathVariable UUID commentId) {
        commentService.deleteComment(commentId);
    }

    @PutMapping("{commentId}")
    public Comment editComment(@PathVariable UUID commentId, @Valid @RequestBody UpdateComment updateComment) {
        return commentService.editComment(commentId, updateComment);
    }
}
