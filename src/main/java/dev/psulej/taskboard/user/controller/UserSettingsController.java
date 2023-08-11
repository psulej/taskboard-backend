package dev.psulej.taskboard.user.controller;

import dev.psulej.taskboard.image.domain.Image;
import dev.psulej.taskboard.image.repository.ImageRepository;
import dev.psulej.taskboard.user.api.UpdateSettings;
import dev.psulej.taskboard.user.domain.User;
import dev.psulej.taskboard.user.domain.UserSettings;
import dev.psulej.taskboard.user.repository.UserRepository;
import dev.psulej.taskboard.user.repository.UserSettingsRepository;
import dev.psulej.taskboard.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/settings")
@AllArgsConstructor
public class UserSettingsController {
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final UserSettingsRepository userSettingsRepository;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadAvatar(
            UpdateSettings updateSettings,
            @RequestParam(name = "file") MultipartFile file
    ) {
        try {

            // avatar
            String fileName = file.getOriginalFilename();
            log.info("Import fileContent {}", fileName);
            Image image = persistImage(file, fileName);
            updateUserImage(image);

            // theme
            persistTheme(updateSettings);

            return new ResponseEntity<>("Avatar upload successful", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Upload failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void persistTheme(UpdateSettings updateSettings) {
        UUID loggedUserId = userService.getLoggedUser().id();
        userSettingsRepository.insert(UserSettings.builder()
                .id(loggedUserId)
                .theme(updateSettings.theme())
                .build());
    }

    private Image persistImage(MultipartFile file, String fileName) throws IOException {
        return imageRepository.insert(
                Image.builder()
                        .id(UUID.randomUUID())
                        .fileName(fileName)
                        .fileContent(new Binary(BsonBinarySubType.BINARY, file.getBytes()))
                        .build()
        );
    }

    private void updateUserImage(Image image) {
        UUID loggedUserId = userService.getLoggedUser().id();
        User user = userRepository.findById(loggedUserId).orElseThrow(() -> new IllegalArgumentException("User not found!"));
        User updatedUser = User.builder()
                .id(user.id())
                .login(user.login())
                .email(user.email())
                .name(user.name())
                .role(user.role())
                .password(user.password())
                .imageId(image.id())
                .build();
        userRepository.save(updatedUser);
    }
}