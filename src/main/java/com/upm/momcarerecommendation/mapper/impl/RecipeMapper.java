package com.upm.momcarerecommendation.mapper.impl;

import com.upm.momcarerecommendation.domain.entity.NutrientsInfo;
import com.upm.momcarerecommendation.domain.dto.RecipeApiResponse;
import com.upm.momcarerecommendation.domain.entity.RecipeEntity;
import com.upm.momcarerecommendation.mapper.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class RecipeMapper implements Mapper<RecipeApiResponse.Recipe, RecipeEntity> {
    @Override
    public RecipeEntity mapToEntity(RecipeApiResponse.Recipe recipe) {
        return RecipeEntity.builder()
                .label(recipe.getLabel())
                .image(recipe.getImage())
                .source(recipe.getSource())
                .url(recipe.getUrl())
                .yield(recipe.getYield())
                .dietLabels(recipe.getDietLabels() != null ? new ArrayList<>(recipe.getDietLabels()) : new ArrayList<>())
                .healthLabels(recipe.getHealthLabels() != null ? new ArrayList<>(recipe.getHealthLabels()) : new ArrayList<>())
                .ingredientLines(recipe.getIngredientLines() != null ? new ArrayList<>(recipe.getIngredientLines()) : new ArrayList<>())
                .cautions(recipe.getCautions() != null ? new ArrayList<>(recipe.getCautions()) : new ArrayList<>())
                .instructionLines(recipe.getInstructionLines() != null ? new ArrayList<>(recipe.getInstructionLines()) : new ArrayList<>())
                .calories(recipe.getCalories())
                .totalTime(recipe.getTotalTime())
                .totalNutrients(mapNutrientsInfo(recipe.getTotalNutrients()))
                .build();
    }
    @Override
    public RecipeApiResponse.Recipe mapToDto(RecipeEntity recipeEntity) {
        RecipeApiResponse.Recipe.RecipeBuilder builder = RecipeApiResponse.Recipe.builder()
                .label(recipeEntity.getLabel())
                .image(recipeEntity.getImage())
                .source(recipeEntity.getSource())
                .url(recipeEntity.getUrl())
                .yield(recipeEntity.getYield())
                .dietLabels(new ArrayList<>(recipeEntity.getDietLabels()))
                .healthLabels(new ArrayList<>(recipeEntity.getHealthLabels()))
                .ingredientLines(new ArrayList<>(recipeEntity.getIngredientLines()))
                .cautions(new ArrayList<>(recipeEntity.getCautions()))
                .instructionLines(new ArrayList<>(recipeEntity.getInstructionLines()))
                .calories(recipeEntity.getCalories())
                .totalTime(recipeEntity.getTotalTime())
                .totalNutrients(mapNutrientsInfoToApiResponse(recipeEntity.getTotalNutrients()));

        return builder.build();
    }



    private static final Map<String, String> NUTRIENT_NAMES = Map.ofEntries(
            Map.entry("FAT", "Fat"),
            Map.entry("CHOCDF", "Carbs"),
            Map.entry("FIBTG", "Fiber"),
            Map.entry("PROCNT", "Protein"),
            Map.entry("NA", "Sodium"),
            Map.entry("CA", "Calcium"),
            Map.entry("MG", "Magnesium"),
            Map.entry("FE", "Iron"),
            Map.entry("ZN", "Zinc"),
            Map.entry("P", "Phosphorus"),
            Map.entry("VITA_RAE", "Vitamin A"),
            Map.entry("VITC", "Vitamin C"),
            Map.entry("VITB6A", "Vitamin B6"),
            Map.entry("FOLAC", "Folic Acid"),
            Map.entry("VITB12", "Vitamin B12"),
            Map.entry("VITD", "Vitamin D"),
            Map.entry("TOCPHA", "Vitamin E"),
            Map.entry("VITK1", "Vitamin K")
    );

    private static final Map<String, String> NUTRIENT_UNITS = Map.ofEntries(
            Map.entry("FAT", "g"),
            Map.entry("CHOCDF", "g"),
            Map.entry("FIBTG", "g"),
            Map.entry("PROCNT", "g"),
            Map.entry("NA", "mg"),
            Map.entry("CA", "mg"),
            Map.entry("MG", "mg"),
            Map.entry("FE", "mg"),
            Map.entry("ZN", "mg"),
            Map.entry("P", "mg"),
            Map.entry("VITA_RAE", "µg"),
            Map.entry("VITC", "mg"),
            Map.entry("VITB6A", "mg"),
            Map.entry("FOLAC", "µg"),
            Map.entry("VITB12", "µg"),
            Map.entry("VITD", "µg"),
            Map.entry("TOCPHA", "mg"),
            Map.entry("VITK1", "µg")
    );

    private static final Map<String, Function<NutrientsInfo, Double>> NUTRIENT_VALUE_ACCESSORS = Map.ofEntries(
            Map.entry("FAT", NutrientsInfo::getFat),
            Map.entry("CHOCDF", NutrientsInfo::getCarbs),
            Map.entry("FIBTG", NutrientsInfo::getFiber),
            Map.entry("PROCNT", NutrientsInfo::getProtein),
            Map.entry("NA", NutrientsInfo::getSodium),
            Map.entry("CA", NutrientsInfo::getCalcium),
            Map.entry("MG", NutrientsInfo::getMagnesium),
            Map.entry("FE", NutrientsInfo::getIron),
            Map.entry("ZN", NutrientsInfo::getZinc),
            Map.entry("P", NutrientsInfo::getPhosphorus),
            Map.entry("VITA_RAE", NutrientsInfo::getVitaminA),
            Map.entry("VITC", NutrientsInfo::getVitaminC),
            Map.entry("VITB6A", NutrientsInfo::getVitaminB6),
            Map.entry("FOLAC", NutrientsInfo::getFolicAcid),
            Map.entry("VITB12", NutrientsInfo::getVitaminB12),
            Map.entry("VITD", NutrientsInfo::getVitaminD),
            Map.entry("TOCPHA", NutrientsInfo::getVitaminE),
            Map.entry("VITK1", NutrientsInfo::getVitaminK)
    );

    // helper methods for mapToEntity method
    private NutrientsInfo mapNutrientsInfo(Map<String, RecipeApiResponse.Nutrient> nutrientsMap) {
        NutrientsInfo.NutrientsInfoBuilder builder = NutrientsInfo.builder();
        if (nutrientsMap != null) {
            for (Map.Entry<String, String> entry : NUTRIENT_NAMES.entrySet()) {
                String key = entry.getKey();
                RecipeApiResponse.Nutrient nutrient = nutrientsMap.get(key);
                if (nutrient != null) {
                    double quantity = nutrient.getQuantity();
                    switch (key) {
                        case "FAT":
                            builder.fat(quantity);
                            break;
                        case "CHOCDF":
                            builder.carbs(quantity);
                            break;
                        case "FIBTG":
                            builder.fiber(quantity);
                            break;
                        case "PROCNT":
                            builder.protein(quantity);
                            break;
                        case "NA":
                            builder.sodium(quantity);
                            break;
                        case "CA":
                            builder.calcium(quantity);
                            break;
                        case "MG":
                            builder.magnesium(quantity);
                            break;
                        case "FE":
                            builder.iron(quantity);
                            break;
                        case "ZN":
                            builder.zinc(quantity);
                            break;
                        case "P":
                            builder.phosphorus(quantity);
                            break;
                        case "VITA_RAE":
                            builder.vitaminA(quantity);
                            break;
                        case "VITC":
                            builder.vitaminC(quantity);
                            break;
                        case "VITB6A":
                            builder.vitaminB6(quantity);
                            break;
                        case "FOLAC":
                            builder.folicAcid(quantity);
                            break;
                        case "VITB12":
                            builder.vitaminB12(quantity);
                            break;
                        case "VITD":
                            builder.vitaminD(quantity);
                            break;
                        case "TOCPHA":
                            builder.vitaminE(quantity);
                            break;
                        case "VITK1":
                            builder.vitaminK(quantity);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return builder.build();
    }

    // helper methods for mapToDto method
    private Map<String, RecipeApiResponse.Nutrient> mapNutrientsInfoToApiResponse(NutrientsInfo nutrientsInfo) {
        Map<String, RecipeApiResponse.Nutrient> nutrientsMap = new HashMap<>();
        if (nutrientsInfo != null) {
            NUTRIENT_NAMES.forEach((key, name) -> {
                Function<NutrientsInfo, Double> valueAccessor = NUTRIENT_VALUE_ACCESSORS.get(key);
                String unit = NUTRIENT_UNITS.get(key);
                Double value = valueAccessor.apply(nutrientsInfo);
                if (value != null) {
                    nutrientsMap.put(key, new RecipeApiResponse.Nutrient(name, value, unit));
                }
            });
        }
        return nutrientsMap;
    }
}