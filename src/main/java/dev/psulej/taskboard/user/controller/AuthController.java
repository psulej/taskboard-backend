package dev.psulej.taskboard.user.controller;
import dev.psulej.taskboard.user.api.LoginRequest;
import dev.psulej.taskboard.user.api.LoginResponse;
import dev.psulej.taskboard.user.api.RegisterRequest;
import dev.psulej.taskboard.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private final UserService userService;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        String token = userService.authenticateAndGetToken(loginRequest.login(), loginRequest.password());
        return new LoginResponse(token);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public void register(@Valid @RequestBody RegisterRequest registerRequest) {
        userService.register(registerRequest);
    }
}
