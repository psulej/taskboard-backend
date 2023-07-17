package dev.psulej.taskboard.user.controller;


import dev.psulej.taskboard.user.domain.Avatar;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/user-settings")
@AllArgsConstructor
public class UserSettingsController {
    @PostMapping("/upload-avatar")
    public ResponseEntity<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            log.info("Import avatar {}", fileName);

            InputStream fileStream = file.getInputStream();

            Avatar avatar = Avatar.builder()
                    .id(UUID.randomUUID())
                    .name(fileName)
                    .avatar(new Binary(BsonBinarySubType.BINARY, file.getBytes()))
                    .build();

            return new ResponseEntity<>("Avatar upload successful", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Upload failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
