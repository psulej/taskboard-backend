package dev.psulej.taskboard.user.service;
import dev.psulej.taskboard.user.api.UserContext;
import dev.psulej.taskboard.user.domain.User;
import dev.psulej.taskboard.user.domain.UserSettings;
import dev.psulej.taskboard.user.repository.UserRepository;
import dev.psulej.taskboard.user.repository.UserSettingsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@AllArgsConstructor
@Service
public class UserContextService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserSettingsRepository userSettingsRepository;

    public UserContext getUserLoggedUserContext() {
        UUID loggedUserId = userService.getLoggedUser().id();

        User user = userRepository.findById(loggedUserId).orElseThrow(() -> new IllegalArgumentException("User not found!"));
        UserSettings userSettings = userSettingsRepository.findByUserId(loggedUserId).orElseThrow(() -> new IllegalArgumentException("User settings not found!"));

        return UserContext.builder()
                .id(user.id())
                .login(user.login())
                .email(user.email())
                .name(user.name())
                .imageId(user.imageId())
                .theme(userSettings.theme())
                .avatarColor(user.avatarColor())
                .build();
    }
}
