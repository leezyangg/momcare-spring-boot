package com.upm.momcarerecommendation.controller;

import com.upm.momcarerecommendation.service.ImageStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageStorageService imageStorageService;

    public ImageController(ImageStorageService imageStorageService) {
        this.imageStorageService = imageStorageService;
    }

    @PostMapping
    public Mono<ResponseEntity<String>> saveImage(@RequestParam String imageUrl, @RequestParam String imageName) {
        return imageStorageService.saveImage(imageUrl, imageName)
                .map(path->ResponseEntity.ok("Image saved successfully at: " + path))
                .onErrorResume(e -> Mono.just(
                        ResponseEntity.internalServerError().body("Failed to save image: " + e.getMessage())
                ));
    }
}
