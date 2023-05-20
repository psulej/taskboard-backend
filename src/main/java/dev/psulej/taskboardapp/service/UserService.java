package dev.psulej.taskboardapp.service;

import dev.psulej.taskboardapp.model.User;
import dev.psulej.taskboardapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getLoggedUser() {
        return userRepository.findByLogin("asmith")
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
