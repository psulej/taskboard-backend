package dev.psulej.taskboard.user.service;

import dev.psulej.taskboard.user.api.UserContext;
import dev.psulej.taskboard.user.domain.User;
import dev.psulej.taskboard.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class UserContextService {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserContext getUserLoggedUserContext() {
        UUID loggedUserId = userService.getLoggedUser().id();
        User user = userRepository.findById(loggedUserId).orElseThrow(() -> new IllegalArgumentException("User not found!"));

        return UserContext.builder()
                .id(user.id())
                .login(user.login())
                .email(user.email())
                .name(user.name())
                .imageId(user.imageId())
                .build();

    }
}
