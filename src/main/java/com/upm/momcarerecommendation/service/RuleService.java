package com.upm.momcarerecommendation.service;

import com.upm.momcarerecommendation.domain.model.MotherRequest;

import java.util.List;
import java.util.Map;

public interface RuleService {
    Map<String, List<String>> getRecipeParams(MotherRequest motherRequest);
    Map<String, List<String>> getExerciseParams(MotherRequest motherRequest);
}
