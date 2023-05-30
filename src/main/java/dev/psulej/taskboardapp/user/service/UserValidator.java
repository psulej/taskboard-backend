package dev.psulej.taskboardapp.user.service;

import dev.psulej.taskboardapp.error.ApplicationError;
import dev.psulej.taskboardapp.error.ApplicationException;
import dev.psulej.taskboardapp.user.dto.RegisterRequest;
import dev.psulej.taskboardapp.user.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserValidator {
    private final UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    void validate(RegisterRequest request) {
        List<ApplicationError> errors = new ArrayList<>();

        if (userRepository.findByLoginIgnoreCase(request.login()).isPresent()) {
            errors.add(ApplicationError.LOGIN_EXISTS);
        }
        if (userRepository.findByEmailIgnoreCase(request.email()).isPresent()) {
            errors.add(ApplicationError.EMAIL_EXISTS);
        }
        if (!errors.isEmpty()) {
            throw new ApplicationException(errors);
        }
    }
}
