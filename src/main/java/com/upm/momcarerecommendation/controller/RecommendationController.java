package com.upm.momcarerecommendation.controller;

import com.upm.momcarerecommendation.domain.MotherRequest;
import com.upm.momcarerecommendation.service.EdamamFoodApiService;
import com.upm.momcarerecommendation.service.RecommendationCoordinatorService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api")
public class RecommendationController {

    private final RecommendationCoordinatorService recommendationCoordinatorService;

    private final EdamamFoodApiService edamamFoodApiService;

    public RecommendationController(RecommendationCoordinatorService recommendationCoordinatorService, EdamamFoodApiService edamamFoodApiService) {
        this.recommendationCoordinatorService = recommendationCoordinatorService;
        this.edamamFoodApiService = edamamFoodApiService;
    }

    @PostMapping("/foodrecommend")
    public String getFoodRecommendation(@RequestBody MotherRequest motherRequest) {
        return recommendationCoordinatorService.getFoodRecommendation(motherRequest);
    }

    // testing endpoint (need to be deleted in production)
    @PostMapping("/testfood")
    public String testFood(@RequestBody Map<String, String> foodQueryMap) {
        return edamamFoodApiService.getFoodRecipe(foodQueryMap);
    }

}
