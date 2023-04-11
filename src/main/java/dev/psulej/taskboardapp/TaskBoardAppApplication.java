package dev.psulej.taskboardapp;



import dev.psulej.taskboardapp.model.Board;
import dev.psulej.taskboardapp.model.Column;
import dev.psulej.taskboardapp.model.Task;
import dev.psulej.taskboardapp.model.User;
import dev.psulej.taskboardapp.repository.BoardRepository;
import dev.psulej.taskboardapp.repository.ColumnRepository;
import dev.psulej.taskboardapp.repository.TaskRepository;
import dev.psulej.taskboardapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.UUID;
import java.util.List;


@SpringBootApplication
public class TaskBoardAppApplication implements CommandLineRunner {


    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final ColumnRepository columnRepository;

    @Autowired
    public TaskBoardAppApplication(BoardRepository boardRepository, UserRepository userRepository, TaskRepository taskRepository, ColumnRepository columnRepository) {
        // BoardController - przetestowanie boarda, zwrocenie go
        // boardRepository.findAll().stream().findFirst().orElse(null);
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.columnRepository = columnRepository;

    }

    public static void main(String[] args) {SpringApplication.run(TaskBoardAppApplication.class, args);}

    @Override
    public void run(String... args) throws Exception {
//        if(userRepository.findAll().isEmpty()) {
//            userRepository.save(new User(UUID.randomUUID(),"jdoe", "test"));
//            userRepository.save(new User(UUID.randomUUID(),"as123", "pass2"));
//        }

        List<User> users = List.of(
                new User(UUID.randomUUID(),"jdoe","testpass"),
                new User(UUID.randomUUID(),"asmith","testpass2")
        );
        userRepository.saveAll(users);

        List<Task> tasks = List.of(
                new Task(UUID.randomUUID(),"tasktitle1","taskdescr1"),
                new Task(UUID.randomUUID(),"tasktitle2","taskdescr2")
        );

        taskRepository.saveAll(tasks);

        List<Column> columns = List.of(
                new Column(UUID.randomUUID(),"col1",tasks)
        );

        columnRepository.saveAll(columns);
        Board board1 = new Board(UUID.randomUUID(),"board1",users,columns);
        boardRepository.save(board1);
    }
}