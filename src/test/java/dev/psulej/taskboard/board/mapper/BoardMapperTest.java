package dev.psulej.taskboard.board.mapper;

import dev.psulej.taskboard.board.api.Board;
import dev.psulej.taskboard.board.api.Column;
import dev.psulej.taskboard.board.api.Task;
import dev.psulej.taskboard.board.domain.BoardEntity;
import dev.psulej.taskboard.board.domain.ColumnEntity;
import dev.psulej.taskboard.board.domain.TaskEntity;
import dev.psulej.taskboard.board.domain.TaskPriority;
import dev.psulej.taskboard.user.api.User;
import dev.psulej.taskboard.user.domain.UserEntity;
import dev.psulej.taskboard.user.domain.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class BoardMapperTest {

    private final UserMapper userMapper = new UserMapper();
    private final TaskMapper taskMapper = new TaskMapper(userMapper);
    private final ColumnMapper columnMapper = new ColumnMapper(taskMapper);
    private final BoardMapper boardMapper = new BoardMapper(columnMapper, userMapper);

    @Test
    @DisplayName("Map board test")
    void shouldMapBoard() {

        UserEntity userEntity = UserEntity.builder()
                .id(UUID.fromString("0dd72a53-17be-4b35-ac87-d2cde83dc9d2"))
                .login("johndoe")
                .email("johndoe@localhost")
                .role(UserRole.USER)
                .name("John Doe")
                .imageId(UUID.fromString("3c9eef1b-d27b-40bf-a504-e997ac877a6e"))
                .build();

        BoardEntity boardEntity = BoardEntity.builder()
                .id(UUID.fromString("fe8bb73e-71f5-11ee-b962-0242ac120002"))
                .name("testBoard")
                .columns(List.of(
                        ColumnEntity.builder()
                                .id(UUID.fromString("873b9f40-71f6-11ee-b962-0242ac120002"))
                                .name("testColumn1")
                                .tasks(
                                        List.of(
                                                TaskEntity.builder()
                                                        .id(UUID.fromString("13fe8fb2-7104-11ee-b962-0242ac120002"))
                                                        .title("testTaskTitle")
                                                        .description("testTaskDescription")
                                                        .assignedUser(userEntity)
                                                        .priority(TaskPriority.NORMAL)
                                                        .build(),
                                                TaskEntity.builder()
                                                        .id(UUID.fromString("f55fe0a8-71f6-11ee-b962-0242ac120002"))
                                                        .title("testTaskTitle2")
                                                        .description("testTaskDescription2")
                                                        .assignedUser(null)
                                                        .priority(TaskPriority.LOW)
                                                        .build()

                                        )
                                )
                                .build(),
                        ColumnEntity.builder()
                                .id(UUID.fromString("692e145a-71f7-11ee-b962-0242ac120002"))
                                .name("testColumn2")
                                .tasks(
                                        List.of(
                                                TaskEntity.builder()
                                                        .id(UUID.fromString("692e1702-71f7-11ee-b962-0242ac120002"))
                                                        .title("testTaskTitle3")
                                                        .description("testTaskDescription3")
                                                        .assignedUser(userEntity)
                                                        .priority(TaskPriority.NORMAL)
                                                        .build(),
                                                TaskEntity.builder()
                                                        .id(UUID.fromString("7444985a-71f7-11ee-b962-0242ac120002"))
                                                        .title("testTaskTitle4")
                                                        .description("testTaskDescription4")
                                                        .assignedUser(userEntity)
                                                        .priority(TaskPriority.HIGH)
                                                        .build()

                                        )
                                )
                                .build()
                ))
                .build();

        Board board = boardMapper.mapBoard(boardEntity);
        assertThat(board).isNotNull();
        assertThat(board.name()).isEqualTo("testBoard");
        assertThat(board.columns()).hasSize(2);

        Column firstColumn = board.columns().get(0);
        assertThat(firstColumn.name()).isEqualTo("testColumn1");
        assertThat(firstColumn.tasks()).hasSize(2);

        Task firstTaskInFirstColumn = firstColumn.tasks().get(0);
        assertThat(firstTaskInFirstColumn.title()).isEqualTo("testTaskTitle");
        assertThat(firstTaskInFirstColumn.description()).isEqualTo("testTaskDescription");
        assertThat(firstTaskInFirstColumn.priority()).isEqualTo(TaskPriority.NORMAL);

        User assignedUserInFirstTask = firstTaskInFirstColumn.assignedUser();
        assertThat(assignedUserInFirstTask).isNotNull();
        assertThat(assignedUserInFirstTask.login()).isEqualTo("johndoe");

        Task secondTaskInFirstColumn = firstColumn.tasks().get(1);
        assertThat(secondTaskInFirstColumn.assignedUser()).isNull();
    }
}