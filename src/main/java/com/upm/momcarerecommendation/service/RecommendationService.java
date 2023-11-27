package com.upm.momcarerecommendation.service;

import com.upm.momcarerecommendation.domain.MotherRequest;

import java.util.Map;

public interface RecommendationService {
    Map<String, String> generateFoodRecommendation(MotherRequest motherRequest);

}
