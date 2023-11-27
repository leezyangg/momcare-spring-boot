package com.upm.momcarerecommendation.service.impl;

import com.upm.momcarerecommendation.domain.MotherRequest;
import com.upm.momcarerecommendation.service.EdamamFoodApiService;
import com.upm.momcarerecommendation.service.RecommendationCoordinatorService;
import com.upm.momcarerecommendation.service.RecommendationService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RecommendationCoordinatorServiceImpl implements RecommendationCoordinatorService {

    private final RecommendationService recommendationService;
    private final EdamamFoodApiService edamamFoodApiService;

    public RecommendationCoordinatorServiceImpl(RecommendationService recommendationService, EdamamFoodApiService edamamFoodApiService) {
        this.recommendationService = recommendationService;
        this.edamamFoodApiService = edamamFoodApiService;
    }

    @Override
    public String getFoodRecommendation(MotherRequest motherRequest) {
        Map<String,String> foodRecommendParam = recommendationService.generateFoodRecommendation(motherRequest);
        return edamamFoodApiService.getFoodRecipe(foodRecommendParam);
    }
}
