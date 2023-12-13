package com.upm.momcarerecommendation.repository;

import com.upm.momcarerecommendation.domain.entity.RecipeEntity;

import java.util.List;
import java.util.Map;

public interface RecipeRepositoryCustom {
    List<RecipeEntity> findRecipesByCriteria(Map<String, Object> criteria);
}
