package com.upm.momcarerecommendation.service;

import com.upm.momcarerecommendation.domain.dto.ExerciseDto;
import com.upm.momcarerecommendation.domain.model.MotherRequest;

import java.util.List;

public interface ExerciseRecommendationService {
    List<ExerciseDto> getExerciseRecommendations(MotherRequest motherRequest);
}
