package com.upm.momcarerecommendation.service.impl;

import com.upm.momcarerecommendation.service.ImageStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageStorageServiceImpl implements ImageStorageService {
    private static final Logger logger = LoggerFactory.getLogger(ImageStorageServiceImpl.class);
    private final WebClient webClient;
    public ImageStorageServiceImpl() {
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(this::increaseCodecMemory)
                .build();
        this.webClient = WebClient.builder()
                .exchangeStrategies(exchangeStrategies)
                .build();
    }

    @Override
    public Mono<String> saveImage(String imageUrl, String imageName) {
        return webClient.get()
                .uri(imageUrl)
                .exchangeToMono(clientResponse -> {
                    String contentType = clientResponse.headers().contentType()
                            .map(MediaType::toString)
                            .orElse("image/jpeg");
                    return clientResponse.bodyToMono(DataBuffer.class)
                            .flatMap(dataBuffer -> saveDataBufferToImage(dataBuffer, contentType, imageName));
                })
                .doOnError(e -> logger.error("Error downloading image: ", e));
    }


    private Mono<String> saveDataBufferToImage(DataBuffer dataBuffer, String contentType, String recipeName) {

        try {
            String imageDirectoryPath = "src/main/resources/static/images/recipes";
            Path path = Paths.get(imageDirectoryPath, sanitizeFileName(recipeName) + getFileTypeFromMineType(contentType));
            Files.copy(dataBuffer.asInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            DataBufferUtils.release(dataBuffer);
            return Mono.just(path.toString());

        } catch (Exception e) {
            DataBufferUtils.release(dataBuffer);
            return Mono.error(e);
        }
    }

    private void increaseCodecMemory(ClientCodecConfigurer clientCodecConfigurer) {
        // increase the memory for the buffer for downloading larger file
        clientCodecConfigurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024); // 16MB
    }

    private String sanitizeFileName(String imageName) {
        // Replace invalid characters with an underscore
        return imageName.replaceAll("[<>\\\\:*|\".]", "_");
    }

    private String getFileTypeFromMineType(String mimeType) {
        // get the file type of the image (png, jpg..) from observing the Mine Type in the header
        return switch (mimeType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            default -> ".jpg";
        };
    }
}
