package com.upm.momcarerecommendation.service;

import com.upm.momcarerecommendation.domain.model.MotherRequest;
import com.upm.momcarerecommendation.domain.model.RecipeApiResponse;
import reactor.core.publisher.Mono;

public interface RecommendationCoordinatorService {
    Mono<RecipeApiResponse> getFoodRecommendation(MotherRequest motherRequest);
    Mono<RecipeApiResponse> getFoodRecommendationFromApi(MotherRequest motherRequest);
}
