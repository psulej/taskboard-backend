package dev.psulej.taskboard.board.mapper;

import dev.psulej.taskboard.board.api.Task;
import dev.psulej.taskboard.board.domain.TaskEntity;
import dev.psulej.taskboard.board.domain.TaskPriority;
import dev.psulej.taskboard.user.domain.UserEntity;
import dev.psulej.taskboard.user.domain.UserRole;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TaskMapperTest {

    private final UserMapper userMapper = new UserMapper();
    private final TaskMapper taskMapper = new TaskMapper(userMapper);

    @Test
    void shouldMapTask() {
        UserEntity userEntity = UserEntity.builder()
                .id(UUID.fromString("f6c6706a-7395-11ee-b962-0242ac120002"))
                .login("johndoe")
                .email("johndoe@localhost")
                .role(UserRole.USER)
                .name("John Doe")
                .imageId(UUID.fromString("f99efc8a-7395-11ee-b962-0242ac120002"))
                .build();

        TaskEntity taskEntity =  TaskEntity.builder()
                .id(UUID.fromString("fab8a376-7397-11ee-b962-0242ac120002"))
                .title("testTaskTitle")
                .description("testTaskDescription")
                .assignedUser(userEntity)
                .priority(TaskPriority.NORMAL)
                .build();

        Task task = taskMapper.mapTask(taskEntity);

        assertNotNull(task);
        assertEquals(taskEntity.id(), task.id());
        assertEquals(taskEntity.title(), task.title());
        assertEquals(taskEntity.description(), task.description());
        assertEquals(taskEntity.priority(), task.priority());
        assertEquals(userEntity.id(), task.assignedUser().id());
    }
}