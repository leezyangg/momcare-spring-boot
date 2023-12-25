package com.upm.momcarerecommendation.service;

import com.upm.momcarerecommendation.domain.model.MotherRequest;
import com.upm.momcarerecommendation.domain.dto.RecipeApiResponse;
import reactor.core.publisher.Mono;

public interface RecipeRecommendationService {
    Mono<RecipeApiResponse> getRecipeRecommendations(MotherRequest motherRequest);
    Mono<RecipeApiResponse> getRecipeRecommendationsFromApi(MotherRequest motherRequest);
}
