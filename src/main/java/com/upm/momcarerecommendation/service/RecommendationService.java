package com.upm.momcarerecommendation.service;

import com.upm.momcarerecommendation.domain.model.MotherRequest;

import java.util.List;
import java.util.Map;

public interface RecommendationService {
    Map<String, List<String>> generateFoodRecommendation(MotherRequest motherRequest);

}
