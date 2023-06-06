package dev.psulej.taskboard.init;

import dev.psulej.taskboard.board.domain.Board;
import dev.psulej.taskboard.board.domain.Column;
import dev.psulej.taskboard.board.domain.Task;
import dev.psulej.taskboard.board.repository.BoardRepository;
import dev.psulej.taskboard.board.repository.ColumnRepository;
import dev.psulej.taskboard.board.repository.TaskRepository;
import dev.psulej.taskboard.user.domain.User;
import dev.psulej.taskboard.user.domain.UserRole;
import dev.psulej.taskboard.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final ColumnRepository columnRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findAll().isEmpty() && boardRepository.findAll().isEmpty() && taskRepository.findAll().isEmpty() && columnRepository.findAll().isEmpty()) {
            List<User> users = List.of(
                    User.builder()
                            .id(UUID.fromString("461c84d0-2233-433b-9784-4bf32cd81d6e"))
                            .login("asmith")
                            .password(passwordEncoder.encode("asmith"))
                            .name("Anna")
                            .email("asmith@yahoo.com")
                            .role(UserRole.USER)
                            .build()
            );

            userRepository.saveAll(users);

            List<Task> column1Tasks = List.of(
                    Task.builder()
                            .id(UUID.fromString("de656f7a-bf69-4ac6-9216-2fb6405d5480"))
                            .title("Col1Task1")
                            .description("Col1Task1")
                            .build(),
                    Task.builder()
                            .id(UUID.fromString("cad0fcc2-0f73-47ea-9de2-92b63270be84"))
                            .title("Col1Task2")
                            .description("Col1Task2")
                            .build()
            );

            List<Task> column2Tasks = List.of(
                    Task.builder()
                            .id(UUID.fromString("6f17d5ae-b931-4de0-a8af-14d134e4c239"))
                            .title("Col2Task1")
                            .description("Col2Task1")
                            .build(),
                    Task.builder()
                            .id(UUID.fromString("b86f70df-485e-4af4-92f0-726ea7944ec3"))
                            .title("Col2Task2")
                            .description("Col2Task2")
                            .build()
            );

            List<Task> column3Tasks = List.of(
                    Task.builder()
                            .id(UUID.fromString("f7efe0b1-4669-46cb-8418-35396f58e797"))
                            .title("Col3Task1")
                            .description("Col3Task1")
                            .build(),
                    Task.builder()
                            .id(UUID.fromString("422ad8ec-8557-49b8-a57e-4653272e4086"))
                            .title("Col3Task2")
                            .description("Col3Task2")
                            .build()
            );

            taskRepository.saveAll(column1Tasks);
            taskRepository.saveAll(column2Tasks);
            taskRepository.saveAll(column3Tasks);

            List<Column> columns = List.of(
                    Column.builder()
                            .id(UUID.fromString("5434c9cb-ac5b-4061-9ed6-5e144fee67d1"))
                            .name("col1")
                            .tasks(column1Tasks)
                            .build(),
                    Column.builder()
                            .id(UUID.fromString("f995beda-0dbc-4bca-bd7c-5ce48dba6602"))
                            .name("col2")
                            .tasks(column2Tasks)
                            .build(),
                    Column.builder()
                            .id(UUID.fromString("580a36f5-fb91-440e-9fb1-bc75fc214fc3"))
                            .name("col3")
                            .tasks(column3Tasks)
                            .build()
            );

            columnRepository.saveAll(columns);

            Board board1 = Board.builder()
                    .id(UUID.fromString("835aa43b-5baa-400f-b7ca-2aca541ba7b7"))
                    .name("board1")
                    .users(users)
                    .columns(columns)
                    .build();

            boardRepository.save(board1);
        }
    }
}
