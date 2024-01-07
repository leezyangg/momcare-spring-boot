package com.upm.momcarerecommendation.service.impl;

import com.upm.momcarerecommendation.service.ImageStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageStorageServiceImpl implements ImageStorageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageStorageServiceImpl.class);

    private final WebClient webClient = WebClient.create();
    private final String imageDirectoryPath = "src/main/resources/static/images/recipes";

    @Override
    public Mono<String> saveImageLocally(String imageUrl) {
        logger.info("Initiating image saving process for URL: {}", imageUrl);
        return webClient.get()
                .uri(imageUrl)
                .retrieve()
                .bodyToMono(byte[].class)
                .publishOn(Schedulers.boundedElastic())
                .handle((imageBytes, sink) -> {
                    logger.info("Received image bytes, preparing to write to file");
                    String fileName = UUID.randomUUID().toString() + "." + getFileExtension(imageUrl);
                    Path imagePath = Paths.get(imageDirectoryPath, fileName);
                    try (FileOutputStream fos = new FileOutputStream(imagePath.toFile())) {
                        fos.write(imageBytes);
                        logger.info("Image saved successfully at {}", imagePath);
                        sink.next(imagePath.toString());
                    } catch (IOException e) {
                        logger.error("Error saving image", e);
                        sink.error(new RuntimeException("Error saving image", e));
                    }
                });
    }

    private String getFileExtension(String url) {
        return StringUtils.getFilenameExtension(url);
    }
}
