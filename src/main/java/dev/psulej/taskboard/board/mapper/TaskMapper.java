package dev.psulej.taskboard.board.mapper;
import dev.psulej.taskboard.board.api.Task;
import dev.psulej.taskboard.board.domain.TaskEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskMapper {

    private final UserMapper userMapper;

    public Task mapTask(TaskEntity task) {
        return Task.builder()
                .id(task.id())
                .title(task.title())
                .description(task.description())
                .assignedUser(task.assignedUser() != null ? userMapper.mapUser(task.assignedUser()) : null)
                .priority(task.priority())
                .build();
    }

}
