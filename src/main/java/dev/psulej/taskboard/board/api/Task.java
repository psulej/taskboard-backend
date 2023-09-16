package dev.psulej.taskboard.board.api;
import dev.psulej.taskboard.comment.api.Comment;
import dev.psulej.taskboard.user.api.UserView;
import lombok.Builder;


import java.util.List;
import java.util.UUID;

@Builder
public record Task(
        UUID id,
        String title,
        String description,
        UserView assignedUser,
        List<Comment> comments
) {
}
