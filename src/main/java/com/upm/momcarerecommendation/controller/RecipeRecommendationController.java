package com.upm.momcarerecommendation.controller;

import com.upm.momcarerecommendation.domain.model.MotherRequest;
import com.upm.momcarerecommendation.domain.dto.RecipeApiResponse;
import com.upm.momcarerecommendation.service.RecipeRecommendationService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/recipe-recommendations")
public class RecipeRecommendationController {
    private final RecipeRecommendationService recipeRecommendationService;
    public RecipeRecommendationController(RecipeRecommendationService recipeRecommendationService) {
        this.recipeRecommendationService = recipeRecommendationService;
    }


    @PostMapping
    public Mono<RecipeApiResponse> getFoodRecommendation(@RequestBody MotherRequest motherRequest) {
        // Query Local DB first, if no Items found, Query FoodAPi and Save the recipe found in Local DB
        return recipeRecommendationService.getRecipeRecommendations(motherRequest);
    }

    @PostMapping("/refresh")
    public Mono<RecipeApiResponse> getFoodRecommendationFromApi(@RequestBody MotherRequest motherRequest) {
        // Query Food APi directly and save within the database
        return recipeRecommendationService.getRecipeRecommendationsFromApi(motherRequest);
    }

}
