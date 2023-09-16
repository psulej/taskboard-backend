package dev.psulej.taskboard.comment.controller;

import dev.psulej.taskboard.comment.api.NewComment;
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
    public void addComment(@PathVariable UUID taskId, @Valid @RequestBody NewComment newComment) {
        commentService.addComment(taskId, newComment);
    }

    @DeleteMapping("{commentId}")
    public void deleteComment(@PathVariable UUID commentId) {
        commentService.deleteComment(commentId);
    }
}
