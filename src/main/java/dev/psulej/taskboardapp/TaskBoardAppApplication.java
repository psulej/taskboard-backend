package dev.psulej.taskboardapp;

import dev.psulej.taskboardapp.model.User;
import dev.psulej.taskboardapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
public class TaskBoardAppApplication implements CommandLineRunner {

    private final UserRepository userRepository;

    @Autowired
    public TaskBoardAppApplication(UserRepository userRepository) {
        this.userRepository = userRepository;
        // BoardController - przetestowanie boarda, zwrocenie go
        // boardRepository.findAll().stream().findFirst().orElse(null);
    }

    public static void main(String[] args) {SpringApplication.run(TaskBoardAppApplication.class, args);}

    @Override
    public void run(String... args) throws Exception {
        if(userRepository.findAll().isEmpty()) {
            userRepository.save(new User(UUID.randomUUID(),"jdoe", "test"));
            userRepository.save(new User(UUID.randomUUID(),"as123", "pass2"));
        }
    }
}