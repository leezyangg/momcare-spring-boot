package com.upm.momcarerecommendation.service;

import com.upm.momcarerecommendation.domain.model.RecipeApiResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface FoodApiService {
    Mono<RecipeApiResponse> getFoodRecipe(Map<String, List<String>> foodQueryMap);
}
