package dev.psulej.taskboard.user.controller;

import dev.psulej.taskboard.user.api.ApplicationTheme;
import dev.psulej.taskboard.user.domain.UserSettingsEntity;
import dev.psulej.taskboard.user.service.UserSettingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/settings")
@RequiredArgsConstructor
public class UserSettingsController {
    private final UserSettingsService userSettingsService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadSettings(
            @RequestParam(name = "file", required=false) MultipartFile file,
            @RequestParam(name = "theme") ApplicationTheme applicationTheme,
            @RequestParam(name = "avatarColor") String avatarColor
    ) {
        try {
            userSettingsService.uploadSettings(file, applicationTheme, avatarColor);
            return new ResponseEntity<>("Settings save successful", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Settings save failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public UserSettingsEntity getUserSettings() {
        return userSettingsService.getUserSettings();
    }

    @DeleteMapping("/avatar")
    public void deleteUserAvatar() {userSettingsService.deleteUserAvatar();}
}
