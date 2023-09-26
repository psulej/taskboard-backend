package dev.psulej.taskboard.comment.service;

import dev.psulej.taskboard.comment.api.Comment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class CommentCreatedPublisher {
    private final SimpMessagingTemplate simpMessagingTemplate;


    public void publish(UUID taskId, Comment comment) {
        String destination = "/topic/tasks/%s/comments".formatted(taskId);
        simpMessagingTemplate.convertAndSend(destination, comment);
    }
}
