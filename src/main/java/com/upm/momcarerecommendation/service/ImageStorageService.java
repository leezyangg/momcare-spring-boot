package com.upm.momcarerecommendation.service;

import reactor.core.publisher.Mono;

public interface ImageStorageService {
    Mono<String> saveImageLocally(String imageUrl);
}
