package dev.psulej.taskboard.user.service;

import dev.psulej.taskboard.image.domain.Image;
import dev.psulej.taskboard.image.repository.ImageRepository;
import dev.psulej.taskboard.user.domain.UserEntity;
import dev.psulej.taskboard.user.domain.UserSettingsEntity;
import dev.psulej.taskboard.user.repository.UserRepository;
import dev.psulej.taskboard.user.repository.UserSettingsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Service
public class UserSettingsService {

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final UserSettingsRepository userSettingsRepository;

    public void uploadSettings(MultipartFile file, String theme, String avatarColor) {
        try {
            if (file != null) {
                String fileName = file.getOriginalFilename();
                log.info("Import fileContent {}", fileName);
                Image image = persistImage(file, fileName);
                updateUserImage(image);
            }
            updateUserSettings(theme, avatarColor);
        } catch (Exception e) {
            log.error("Upload failed: " + e.getMessage(), e);
        }
    }


    private void updateUserSettings(String theme, String avatarColor) {
        UUID loggedUserId = userService.getLoggedUser().id();

        userSettingsRepository.findByUserId(loggedUserId)
                .map(userSettings -> userSettings.toBuilder()
                        .theme(theme)
                        .build()
                )
                .ifPresent(userSettingsRepository::save);

        userRepository.findById(loggedUserId)
                .map(user -> user.toBuilder()
                        .avatarColor(avatarColor)
                        .build()
                )
                .ifPresent(userRepository::save);
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
        UserEntity user = userRepository.findById(loggedUserId).orElseThrow(() -> new IllegalArgumentException("User not found!"));
        UserEntity updatedUser = UserEntity.builder()
                .id(user.id())
                .login(user.login())
                .email(user.email())
                .name(user.name())
                .role(user.role())
                .password(user.password())
                .imageId(image.id())
                .avatarColor(user.avatarColor())
                .build();
        userRepository.save(updatedUser);
    }

    public UserSettingsEntity getUserSettings() {
        UUID loggedUserId = userService.getLoggedUser().id();
        return userSettingsRepository.findById(loggedUserId).orElseThrow(() -> new IllegalArgumentException("Logged user not found!"));
    }

    public void deleteUserAvatar() {
        UUID loggedUserId = userService.getLoggedUser().id();
        UserEntity user = userRepository.findById(loggedUserId).orElseThrow(() -> new IllegalArgumentException("User not found!"));
        UserEntity updatedUser = UserEntity.builder()
                .id(user.id())
                .login(user.login())
                .email(user.email())
                .name(user.name())
                .role(user.role())
                .password(user.password())
                .imageId(null)
                .avatarColor(user.avatarColor())
                .build();
        userRepository.save(updatedUser);
    }
}
