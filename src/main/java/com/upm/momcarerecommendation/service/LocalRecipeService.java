package com.upm.momcarerecommendation.service;

import com.upm.momcarerecommendation.domain.entity.RecipeEntity;
import com.upm.momcarerecommendation.domain.dto.RecipeApiResponse;

import java.util.List;
import java.util.Map;

public interface LocalRecipeService {
    void saveRecipes(List<RecipeEntity> recipeEntities);
    void processAndSaveRecipes(RecipeApiResponse recipeApiResponse);
    List<RecipeEntity> findRecipesByCriteria(Map<String, Object> criteria);
}
