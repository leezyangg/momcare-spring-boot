package com.upm.momcarerecommendation.controller;

import com.upm.momcarerecommendation.domain.model.MotherRequest;
import com.upm.momcarerecommendation.domain.model.RecipeApiResponse;
import com.upm.momcarerecommendation.service.RecommendationCoordinatorService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api")
public class RecommendationController {
    private final RecommendationCoordinatorService recommendationCoordinatorService;
    public RecommendationController(RecommendationCoordinatorService recommendationCoordinatorService) {
        this.recommendationCoordinatorService = recommendationCoordinatorService;
    }

    // query local first then query api if non-exist
    @PostMapping("/foodrecommend")
    public Mono<RecipeApiResponse> getFoodRecommendation(@RequestBody MotherRequest motherRequest) {
        return recommendationCoordinatorService.getFoodRecommendation(motherRequest);
    }

    // query api directly
    @PostMapping("/foodrecommendapi")
    public Mono<RecipeApiResponse> getFoodRecommendationFromApi(@RequestBody MotherRequest motherRequest) {
        return recommendationCoordinatorService.getFoodRecommendationFromApi(motherRequest);
    }

}
