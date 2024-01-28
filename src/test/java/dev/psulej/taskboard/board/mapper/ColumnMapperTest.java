package dev.psulej.taskboard.board.mapper;

import dev.psulej.taskboard.board.api.Column;
import dev.psulej.taskboard.board.api.Task;
import dev.psulej.taskboard.board.domain.ColumnEntity;
import dev.psulej.taskboard.board.domain.TaskEntity;
import dev.psulej.taskboard.board.domain.TaskPriority;
import dev.psulej.taskboard.user.domain.UserEntity;
import dev.psulej.taskboard.user.domain.UserRole;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.UUID;

class ColumnMapperTest {
    private final UserMapper userMapper = new UserMapper();
    private final TaskMapper taskMapper = new TaskMapper(userMapper);
    private final ColumnMapper columnMapper = new ColumnMapper(taskMapper);

    @Test
    void shouldMapColumn() {
        // given
        UserEntity userEntity = UserEntity.builder()
                .id(UUID.fromString("122aa72a-7393-11ee-b962-0242ac120002"))
                .login("johndoe")
                .email("johndoe@localhost")
                .role(UserRole.USER)
                .name("John Doe")
                .imageId(UUID.fromString("1cdc1cd0-7393-11ee-b962-0242ac120002"))
                .build();

        ColumnEntity columnEntity = ColumnEntity.builder()
                .id(UUID.fromString("17123352-7393-11ee-b962-0242ac120002"))
                .name("testColumn1")
                .tasks(
                        List.of(
                                TaskEntity.builder()
                                        .id(UUID.fromString("28238812-7393-11ee-b962-0242ac120002"))
                                        .title("testTaskTitle")
                                        .description("testTaskDescription")
                                        .assignedUser(userEntity)
                                        .priority(TaskPriority.NORMAL)
                                        .build(),
                                TaskEntity.builder()
                                        .id(UUID.fromString("306b9e60-7393-11ee-b962-0242ac120002"))
                                        .title("testTaskTitle2")
                                        .description("testTaskDescription2")
                                        .priority(TaskPriority.LOW)
                                        .build()

                        )
                )
                .build();

        // when
        Column column = columnMapper.mapColumn(columnEntity);

        // then
        assertThat(column).isNotNull();
        assertThat(column.name()).isEqualTo("testColumn1");
        assertThat(column.tasks()).hasSize(2);

        Task firstTask = column.tasks().get(0);
        assertThat(firstTask.title()).isEqualTo("testTaskTitle");
        assertThat(firstTask.description()).isEqualTo("testTaskDescription");
        assertThat(firstTask.priority()).isEqualTo(TaskPriority.NORMAL);
        assertThat(firstTask.assignedUser()).isNotNull();
        assertThat(firstTask.assignedUser().login()).isEqualTo("johndoe");
        assertThat(firstTask.assignedUser().imageId().toString()).isEqualTo("1cdc1cd0-7393-11ee-b962-0242ac120002");
    }

    @Test
    void shouldMapColumnWithMissingTasks() {
        ColumnEntity columnEntity = ColumnEntity.builder()
                .id(UUID.fromString("17123352-7393-11ee-b962-0242ac120002"))
                .name("testColumn1")
                .build();

        Column column = columnMapper.mapColumn(columnEntity);
        assertThat(column).isNotNull();
        assertThat(column.tasks()).isEmpty();
    }

    @Test
    void shouldMapColumnWithUnassignedTasks() {
        ColumnEntity columnEntity = ColumnEntity.builder()
                .id(UUID.fromString("17123352-7393-11ee-b962-0242ac120002"))
                .name("testColumn1")
                .tasks(List.of(
                        TaskEntity.builder()
                                .id(UUID.fromString("337bb26e-7395-11ee-b962-0242ac120002"))
                                .title("testTaskTitle")
                                .description("testTaskDescription")
                                .priority(TaskPriority.NORMAL)
                                .build(),
                        TaskEntity.builder()
                                .id(UUID.fromString("38bc4c20-7395-11ee-b962-0242ac120002"))
                                .title("testTaskTitle2")
                                .description("testTaskDescription2")
                                .priority(TaskPriority.LOW)
                                .build()
                ))
                .build();

        Column column = columnMapper.mapColumn(columnEntity);
        assertThat(column).isNotNull();

        Task taskWithNullUser = column.tasks().get(0);
        assertThat(taskWithNullUser.assignedUser()).isNull();
    }
}