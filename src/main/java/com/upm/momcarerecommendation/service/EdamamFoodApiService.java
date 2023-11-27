package com.upm.momcarerecommendation.service;

import java.util.Map;

public interface EdamamFoodApiService {
    String getFoodRecipe(Map<String, String> foodQueryParam);
}
