package com.upm.momcarerecommendation.service.impl;

import com.upm.momcarerecommendation.domain.entity.RecipeEntity;
import com.upm.momcarerecommendation.domain.model.MotherRequest;
import com.upm.momcarerecommendation.domain.model.Range;
import com.upm.momcarerecommendation.domain.model.RecipeApiResponse;
import com.upm.momcarerecommendation.mapper.Mapper;
import com.upm.momcarerecommendation.service.FoodApiService;
import com.upm.momcarerecommendation.service.LocalRecipeService;
import com.upm.momcarerecommendation.service.RecommendationCoordinatorService;
import com.upm.momcarerecommendation.service.RecommendationService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecommendationCoordinatorServiceImpl implements RecommendationCoordinatorService {
    private final RecommendationService recommendationService;
    private final FoodApiService foodApiService;
    private final LocalRecipeService localRecipeService;
    private final Mapper<RecipeApiResponse.Recipe, RecipeEntity> recipeMapper;

    public RecommendationCoordinatorServiceImpl(RecommendationService recommendationService, FoodApiService foodApiService, LocalRecipeService localRecipeService, Mapper<RecipeApiResponse.Recipe, RecipeEntity> recipeMapper) {
        this.recommendationService = recommendationService;
        this.foodApiService = foodApiService;
        this.localRecipeService = localRecipeService;
        this.recipeMapper = recipeMapper;
    }


    @Override
    public Mono<RecipeApiResponse> getFoodRecommendation(MotherRequest motherRequest) {
        Map<String,List<String>> foodRecommendParam = recommendationService.generateFoodRecommendation(motherRequest);
        Map<String, Object> dbQueryParams = transformForDatabaseQuery(foodRecommendParam);

        // printing for debug:
        System.out.println("FoodRecommendParam: " + foodRecommendParam);
        System.out.println("DbQueryParams: "+ dbQueryParams);

        List<RecipeEntity> localRecipes = localRecipeService.findRecipesByCriteria(dbQueryParams);

        if (localRecipes.size() == 10) {
            RecipeApiResponse localResponse = convertToRecipeApiResponse(localRecipes);
            return Mono.just(localResponse);

        } else {
            Mono<RecipeApiResponse> apiResponse = foodApiService.getFoodRecipe(foodRecommendParam);
            return apiResponse.doOnNext(localRecipeService::processAndSaveRecipes);
        }
    }

    @Override
    public Mono<RecipeApiResponse> getFoodRecommendationFromApi(MotherRequest motherRequest) {
        Map<String,List<String>> foodRecommendParam = recommendationService.generateFoodRecommendation(motherRequest);

        // printing for debug:
        System.out.println("FoodRecommendParam: " + foodRecommendParam);

        Mono<RecipeApiResponse> apiResponse = foodApiService.getFoodRecipe(foodRecommendParam);
        return apiResponse.doOnNext(localRecipeService::processAndSaveRecipes);
    }


    // transform the param to suit database dynamic query
    private Map<String, Object> transformForDatabaseQuery(Map<String, List<String>> recommendationMap) {
        Map<String, Object> dbQueryParams = new HashMap<>();
        Map<String, String> keyMapping = Map.of(
                "health", "healthLabels",
                "diet", "dietLabels",
                "excluded", "ingredientLines"
                // TODO: Add more mappings as needed
        );

        for (Map.Entry<String, List<String>> entry : recommendationMap.entrySet()) {
            String droolsKey = entry.getKey();
            List<String> values = entry.getValue();

            String dbKey = keyMapping.getOrDefault(droolsKey, droolsKey);

            if (dbKey.equals("healthLabels") || dbKey.equals("dietLabels") || dbKey.equals("ingredientLines")) {
                dbQueryParams.put(dbKey, values);

            } else {
                List<Range> ranges = new ArrayList<>();
                for (String value : values) {
                    Range range = convertToRangeObject(value);
                    if (range != null) {
                        ranges.add(range);
                    }
                }
                dbQueryParams.put(dbKey, ranges);
            }
        }

        return dbQueryParams;
    }

    private Range convertToRangeObject(String value) {
        if (value.contains("-")) {
            // For range values, split into a double array [min, max]
            String[] rangeParts = value.split("-");
            try {
                double min = Double.parseDouble(rangeParts[0]);
                double max = Double.parseDouble(rangeParts[1]);
                return new Range(min, max);
            } catch (NumberFormatException e) {
                System.err.println("Error parsing range value: " + value);
                return null;
            }

        } else if (value.endsWith("+")) {
            // For minimum value, parse the number part
            try {
                double min = Double.parseDouble(value.replace("+", ""));
                return new Range(min, null);
            } catch (NumberFormatException e) {
                System.err.println("Error parsing minimum value: " + value);
                return null;
            }

        } else {
            // For a single value, interpret it as a maximum limit
            try {
                double max = Double.parseDouble(value);
                return new Range(null, max);
            } catch (NumberFormatException e) {
                System.err.println("Error parsing maximum limit value: " + value);
                return null;
            }
        }
    }

    private RecipeApiResponse convertToRecipeApiResponse(List<RecipeEntity> localRecipes) {
        List<RecipeApiResponse.Hit> hits = new ArrayList<>();

        for (RecipeEntity localRecipe : localRecipes) {
            RecipeApiResponse.Recipe apiRecipe = recipeMapper.mapToModel(localRecipe);
            hits.add(new RecipeApiResponse.Hit(apiRecipe));
        }

        return new RecipeApiResponse(hits);
    }
}
