package dev.psulej.taskboard.user.service;

import dev.psulej.taskboard.security.ApplicationUserDetails;
import dev.psulej.taskboard.security.TokenProvider;
import dev.psulej.taskboard.user.domain.UserRole;
import dev.psulej.taskboard.user.api.RegisterRequest;
import dev.psulej.taskboard.user.domain.UserSettingsEntity;
import dev.psulej.taskboard.user.exception.UserCannotBeExtracted;
import dev.psulej.taskboard.user.exception.UserNotFoundException;
import dev.psulej.taskboard.user.repository.UserRepository;
import dev.psulej.taskboard.user.domain.UserEntity;
import dev.psulej.taskboard.user.repository.UserSettingsRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final UserValidator userValidator;
    private final UserSettingsRepository userSettingsRepository;

    public UUID getLoggedUserId() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .filter(principal -> principal.getClass().isAssignableFrom(ApplicationUserDetails.class))
                .map(principal -> (ApplicationUserDetails) principal)
                .map(ApplicationUserDetails::getId)
                .orElseThrow(() -> new UserCannotBeExtracted("User cannot be extracted"));
    }

    public UserEntity getLoggedUser() {
        String login = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .filter(principal -> principal.getClass().isAssignableFrom(ApplicationUserDetails.class))
                .map(principal -> (ApplicationUserDetails) principal)
                .map(ApplicationUserDetails::getLogin)
                .orElseThrow(() -> new UserCannotBeExtracted("User cannot be extracted"));
        return findUserByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public Optional<UserEntity> findUserByLogin(String login) {
        return userRepository.findByLoginIgnoreCase(login);
    }

    public void register(RegisterRequest registerRequest) {
        userValidator.validate(registerRequest);

        UUID registeredUserUUID = UUID.randomUUID();

        UserEntity registerUser = UserEntity.builder()
                .id(registeredUserUUID)
                .login(registerRequest.login())
                .password(passwordEncoder.encode(registerRequest.password()))
                .name(registerRequest.name())
                .email(registerRequest.email())
                .role(UserRole.USER)
                .imageId(null)
                .avatarColor(getRandomAvatarColor())
                .build();

        UserSettingsEntity userSettings = UserSettingsEntity.builder()
                .userId(registeredUserUUID)
                .theme("dark")
                .build();

        userSettingsRepository.save(userSettings);
        //if (true) throw new IllegalArgumentException("Failed");
        userRepository.save(registerUser);
    }

    public String authenticateAndGetToken(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return tokenProvider.generate(authentication);
    }

    public String getRandomAvatarColor() {
        String[] colorArray = {
                "#B80000", "#DB3E00", "#FCCB00", "#008B02",
                "#006B76", "#1273DE", "#004DCF", "#5300EB",
                "#EB9694", "#FAD0C3", "#FEF3BD", "#C1E1C5",
                "#BEDADC", "#C4DEF6", "#BED3F3", "#D4C4FB"
        };

        Random random = new Random();
        return colorArray[random.nextInt(colorArray.length)];
    }
}
