package com.upm.momcarerecommendation.controller;

import com.upm.momcarerecommendation.domain.dto.ExerciseDto;
import com.upm.momcarerecommendation.domain.model.MotherRequest;
import com.upm.momcarerecommendation.service.ExerciseRecommendationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/exercise-recommendations")
public class ExerciseRecommendationController {
    private final ExerciseRecommendationService exerciseRecommendationService;
    public ExerciseRecommendationController(ExerciseRecommendationService exerciseRecommendationService) {
        this.exerciseRecommendationService = exerciseRecommendationService;
    }

    @PostMapping
    public List<ExerciseDto> getExerciseRecommendation(@RequestBody MotherRequest motherRequest){
        return exerciseRecommendationService.getExerciseRecommendations(motherRequest);
    }

}
