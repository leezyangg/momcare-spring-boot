package com.upm.momcarerecommendation.service.impl;

import com.upm.momcarerecommendation.domain.entity.RecipeEntity;
import com.upm.momcarerecommendation.domain.model.MotherRequest;
import com.upm.momcarerecommendation.domain.model.Range;
import com.upm.momcarerecommendation.domain.dto.RecipeApiResponse;
import com.upm.momcarerecommendation.mapper.Mapper;
import com.upm.momcarerecommendation.service.RecipeApiService;
import com.upm.momcarerecommendation.service.LocalRecipeService;
import com.upm.momcarerecommendation.service.RecipeRecommendationService;
import com.upm.momcarerecommendation.service.RuleService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecipeRecommendationServiceImpl implements RecipeRecommendationService {
    private final RuleService ruleService;
    private final RecipeApiService recipeApiService;
    private final LocalRecipeService localRecipeService;
    private final Mapper<RecipeApiResponse.Recipe, RecipeEntity> recipeMapper;

    public RecipeRecommendationServiceImpl(RuleService ruleService, RecipeApiService recipeApiService, LocalRecipeService localRecipeService, Mapper<RecipeApiResponse.Recipe, RecipeEntity> recipeMapper) {
        this.ruleService = ruleService;
        this.recipeApiService = recipeApiService;
        this.localRecipeService = localRecipeService;
        this.recipeMapper = recipeMapper;
    }


    @Override
    public Mono<RecipeApiResponse> getRecipeRecommendations(MotherRequest motherRequest) {
        Map<String,List<String>> apiQueryParams = ruleService.getRecipeParams(motherRequest);
        Map<String, Object> dbQueryParams = transformForDatabaseQuery(apiQueryParams);
        List<RecipeEntity> localRecipeEntities = localRecipeService.findRecipesByCriteria(dbQueryParams);

        if (localRecipeEntities.size() == 10) {
            System.out.println("DbQueryParams: "+ dbQueryParams);
            RecipeApiResponse localResponse = convertToRecipeApiResponse(localRecipeEntities);
            return Mono.just(localResponse);

        } else {
            System.out.println("FoodRecommendParam: " + apiQueryParams);
            Mono<RecipeApiResponse> apiResponse = recipeApiService.getRecipes(apiQueryParams);
            return apiResponse.doOnNext(localRecipeService::processAndSaveRecipes);
        }
    }

    @Override
    public Mono<RecipeApiResponse> getRecipeRecommendationsFromApi(MotherRequest motherRequest) {
        Map<String,List<String>> foodRecommendParam = ruleService.getRecipeParams(motherRequest);
        System.out.println("FoodRecommendParam: " + foodRecommendParam);
        Mono<RecipeApiResponse> apiResponse = recipeApiService.getRecipes(foodRecommendParam);
        return apiResponse.doOnNext(localRecipeService::processAndSaveRecipes);
    }




    // helper method to transform query to suit db query
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


    private RecipeApiResponse convertToRecipeApiResponse(List<RecipeEntity> localRecipeEntities) {
        List<RecipeApiResponse.Hit> hits = new ArrayList<>();

        for (RecipeEntity localRecipeEntity : localRecipeEntities) {
            RecipeApiResponse.Recipe apiRecipe = recipeMapper.mapToDto(localRecipeEntity);
            hits.add(new RecipeApiResponse.Hit(apiRecipe));
        }

        return new RecipeApiResponse(hits);
    }
}
