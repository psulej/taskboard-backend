package dev.psulej.taskboardapp;

import dev.psulej.taskboardapp.models.User;
import dev.psulej.taskboardapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskboardAppApplication implements CommandLineRunner {

    private final UserRepository userRepository;

    @Autowired
    public TaskboardAppApplication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {SpringApplication.run(TaskboardAppApplication.class, args);}

    @Override
    public void run(String... args) throws Exception {
        if(userRepository.findAll().isEmpty()) {
            userRepository.save(new User(1L,"jdoe", "test"));
            userRepository.save(new User(2L,"as123", "pass2"));
        }
    }
}
