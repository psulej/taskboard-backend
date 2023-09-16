package dev.psulej.taskboard.user.controller;
import dev.psulej.taskboard.user.domain.UserSettingsEntity;
import dev.psulej.taskboard.user.service.UserSettingsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/settings")
@AllArgsConstructor
public class UserSettingsController {
    private final UserSettingsService userSettingsService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadSettings(
            @RequestParam(name = "file", required=false) MultipartFile file,
            @RequestParam(name = "theme") String theme,
            @RequestParam(name = "avatarColor") String avatarColor
    ) {
        try {
            userSettingsService.uploadSettings(file, theme, avatarColor);
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
