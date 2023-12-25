package com.upm.momcarerecommendation.service;

import com.upm.momcarerecommendation.domain.dto.RecipeApiResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface RecipeApiService {
    Mono<RecipeApiResponse> getRecipes(Map<String, List<String>> foodQueryMap);
}
