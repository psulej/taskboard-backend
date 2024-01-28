package dev.psulej.taskboard.image.controller;
import dev.psulej.taskboard.image.domain.Image;
import dev.psulej.taskboard.image.repository.ImageRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLConnection;
import java.time.Duration;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/public/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageRepository imageRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getImage(@PathVariable UUID id) {
        Image image = imageRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Image not found for userId: " + id));
        String contentType = URLConnection.guessContentTypeFromName(image.fileName());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .header(ContentDisposition.inline().filename(image.fileName()).build().toString())
                .cacheControl(CacheControl.maxAge(Duration.ofDays(365)))
                .body(image.fileContent().getData());
    }
}
