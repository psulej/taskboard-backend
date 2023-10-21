package dev.psulej.taskboard.user.controller;

import dev.psulej.taskboard.user.api.UserContext;
import dev.psulej.taskboard.user.service.UserContextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user-context")
@RequiredArgsConstructor
public class UserContextController {

    private final UserContextService userContextService;

    @GetMapping
    public UserContext getUserLoggedUserContext() {
        return userContextService.getUserLoggedUserContext();
    }
}
