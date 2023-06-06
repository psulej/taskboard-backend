package dev.psulej.taskboard.user.service;

import dev.psulej.taskboard.security.ApplicationUserDetails;
import dev.psulej.taskboard.security.TokenProvider;
import dev.psulej.taskboard.user.domain.UserRole;
import dev.psulej.taskboard.user.api.RegisterRequest;
import dev.psulej.taskboard.user.repository.UserRepository;
import dev.psulej.taskboard.user.domain.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final UserValidator userValidator;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenProvider tokenProvider, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userValidator = userValidator;
    }


    public User getLoggedUser() {
        String login = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .filter(principal -> principal.getClass().isAssignableFrom(ApplicationUserDetails.class))
                .map(principal -> (ApplicationUserDetails) principal)
                .map(ApplicationUserDetails::getLogin)
                .orElseThrow(() -> new IllegalStateException("User cannot be extracted"));
        return findUserByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public Optional<User> findUserByLogin(String login) {
        return userRepository.findByLoginIgnoreCase(login);
    }

    public void register(RegisterRequest registerRequest) {
        Optional<User> t = Optional.empty();

        userValidator.validate(registerRequest);

        User registerUser = User.builder()
                .id(UUID.randomUUID())
                .login(registerRequest.login())
                .password(passwordEncoder.encode(registerRequest.password()))
                .name(registerRequest.name())
                .email(registerRequest.email())
                .role(UserRole.USER)
                .build();

        userRepository.save(registerUser);
    }

    public String authenticateAndGetToken(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return tokenProvider.generate(authentication);
    }
}
