package dev.psulej.taskboard.user.controller;
import dev.psulej.taskboard.user.domain.Avatar;
import dev.psulej.taskboard.user.repository.AvatarRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/user-settings")
@AllArgsConstructor
public class UserSettingsController {

    private final AvatarRepository avatarRepository;

    @PostMapping("/upload-avatar")
    public ResponseEntity<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            log.info("Import avatar {}", fileName);

            Avatar avatar = Avatar.builder()
                    .id(UUID.randomUUID())
                    .name(fileName)
                    .avatar(new Binary(BsonBinarySubType.BINARY, file.getBytes()))
                    .build();

            avatarRepository.insert(avatar);

            return new ResponseEntity<>("Avatar upload successful", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Upload failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/avatar/{id}")
    public Avatar getAvatar(@PathVariable UUID id) {
        return avatarRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Avatar not found for id: " + id));
    }

}
