package dev.psulej.taskboardapp.user.service;

import dev.psulej.taskboardapp.security.TokenProvider;
import dev.psulej.taskboardapp.user.domain.UserRole;
import dev.psulej.taskboardapp.user.dto.RegisterRequest;
import dev.psulej.taskboardapp.user.exception.DuplicatedUserInfoException;
import dev.psulej.taskboardapp.user.repository.UserRepository;
import dev.psulej.taskboardapp.user.domain.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }


    public User getLoggedUser() {
        return findUserByLogin("asmith")
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public Optional<User> findUserByLogin(String login) {
        return userRepository.findByLoginIgnoreCase(login);
    }

    public void register(RegisterRequest registerRequest) {
        Optional<User> t = Optional.empty();

        if (userRepository.findByLoginIgnoreCase(registerRequest.login()).isPresent()) {
            throw new DuplicatedUserInfoException(String.format("Login %s already been used", registerRequest.login()));
        }
        if (userRepository.findByEmailIgnoreCase(registerRequest.email()).isPresent()) {
            throw new DuplicatedUserInfoException(String.format("Email %s already been used", registerRequest.email()));
        }

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
