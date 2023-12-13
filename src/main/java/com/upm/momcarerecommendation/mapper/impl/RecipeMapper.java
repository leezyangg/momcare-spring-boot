package com.upm.momcarerecommendation.mapper.impl;

import com.upm.momcarerecommendation.domain.entity.NutrientsInfo;
import com.upm.momcarerecommendation.domain.entity.RecipeEntity;
import com.upm.momcarerecommendation.domain.model.RecipeApiResponse;
import com.upm.momcarerecommendation.mapper.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
                .dietLabels(new ArrayList<>(recipe.getDietLabels()))
                .healthLabels(new ArrayList<>(recipe.getHealthLabels()))
                .ingredientLines(new ArrayList<>(recipe.getIngredientLines()))
                .calories(recipe.getCalories())
                .totalTime(recipe.getTotalTime())
                .totalNutrients(mapNutrientsInfo(recipe.getTotalNutrients()))
                .build();
    }
    @Override
    public RecipeApiResponse.Recipe mapToModel(RecipeEntity recipeEntity) {
        RecipeApiResponse.Recipe.RecipeBuilder builder = RecipeApiResponse.Recipe.builder()
                .label(recipeEntity.getLabel())
                .image(recipeEntity.getImage())
                .source(recipeEntity.getSource())
                .url(recipeEntity.getUrl())
                .yield(recipeEntity.getYield())
                .dietLabels(new ArrayList<>(recipeEntity.getDietLabels()))
                .healthLabels(new ArrayList<>(recipeEntity.getHealthLabels()))
                .ingredientLines(new ArrayList<>(recipeEntity.getIngredientLines()))
                .calories(recipeEntity.getCalories())
                .totalTime(recipeEntity.getTotalTime())
                .totalNutrients(mapNutrientsInfoToApiResponse(recipeEntity.getTotalNutrients()));

        return builder.build();
    }



    // helper methods for mapToEntity method
    private NutrientsInfo mapNutrientsInfo(Map<String, RecipeApiResponse.Nutrient> nutrientsMap) {
        NutrientsInfo.NutrientsInfoBuilder builder = NutrientsInfo.builder();

        if (nutrientsMap != null) {
            builder
                    .fat(getNutrientQuantity(nutrientsMap, "FAT"))
                    .carbs(getNutrientQuantity(nutrientsMap, "CHOCDF"))
                    .fiber(getNutrientQuantity(nutrientsMap, "FIBTG"))
                    .protein(getNutrientQuantity(nutrientsMap, "PROCNT"))
                    .sodium(getNutrientQuantity(nutrientsMap, "NA"))
                    .calcium(getNutrientQuantity(nutrientsMap, "CA"))
                    .magnesium(getNutrientQuantity(nutrientsMap, "MG"))
                    .iron(getNutrientQuantity(nutrientsMap, "FE"))
                    .zinc(getNutrientQuantity(nutrientsMap, "ZN"))
                    .vitaminA(getNutrientQuantity(nutrientsMap, "VITA_RAE"))
                    .vitaminC(getNutrientQuantity(nutrientsMap, "VITC"))
                    .vitaminB6(getNutrientQuantity(nutrientsMap, "VITB6A"))
                    .folicAcid(getNutrientQuantity(nutrientsMap, "FOL"))
                    .vitaminB12(getNutrientQuantity(nutrientsMap, "VITB12"))
                    .vitaminD(getNutrientQuantity(nutrientsMap, "VITD"))
                    .vitaminE(getNutrientQuantity(nutrientsMap, "TOCPHA"));
        }

        return builder.build();
    }
    private Double getNutrientQuantity(Map<String, RecipeApiResponse.Nutrient> nutrientsMap, String nutrientKey) {
        RecipeApiResponse.Nutrient nutrient = nutrientsMap.get(nutrientKey);
        return nutrient != null ? nutrient.getQuantity() : null;
    }

    // helper methods for mapToModel method
    private Map<String, RecipeApiResponse.Nutrient> mapNutrientsInfoToApiResponse(NutrientsInfo nutrientsInfo) {
        Map<String, RecipeApiResponse.Nutrient> nutrientsMap = new HashMap<>();
        if (nutrientsInfo != null) {
            nutrientsMap.put("FAT",        new RecipeApiResponse.Nutrient("Fat", nutrientsInfo.getFat(), "g"));
            nutrientsMap.put("FASAT",      new RecipeApiResponse.Nutrient("Saturated", nutrientsInfo.getFat(), "g"));
            nutrientsMap.put("FATRN",      new RecipeApiResponse.Nutrient("Trans", nutrientsInfo.getFat(), "g"));
            nutrientsMap.put("FAMS",       new RecipeApiResponse.Nutrient("Monounsaturated", nutrientsInfo.getFat(), "g"));
            nutrientsMap.put("FAPU",       new RecipeApiResponse.Nutrient("Polyunsaturated", nutrientsInfo.getFat(), "g"));
            nutrientsMap.put("CHOCDF",     new RecipeApiResponse.Nutrient("Carbs", nutrientsInfo.getCarbs(), "g"));
            nutrientsMap.put("CHOCDF.net", new RecipeApiResponse.Nutrient("Carbohydrates (net)", nutrientsInfo.getCarbs(), "g"));
            nutrientsMap.put("FIBTG",      new RecipeApiResponse.Nutrient("Fiber", nutrientsInfo.getCarbs(), "g"));
            nutrientsMap.put("SUGAR",      new RecipeApiResponse.Nutrient("Sugars", nutrientsInfo.getCarbs(), "g"));
            nutrientsMap.put("PROCNT",     new RecipeApiResponse.Nutrient("Protein", nutrientsInfo.getProtein(), "g"));
            nutrientsMap.put("CHOLE",      new RecipeApiResponse.Nutrient("Cholesterol", nutrientsInfo.getProtein(), "mg"));
            nutrientsMap.put("NA",         new RecipeApiResponse.Nutrient("Sodium", nutrientsInfo.getProtein(), "mg"));
            nutrientsMap.put("CA",         new RecipeApiResponse.Nutrient("Calcium", nutrientsInfo.getProtein(), "mg"));
            nutrientsMap.put("MG",         new RecipeApiResponse.Nutrient("Magnesium", nutrientsInfo.getProtein(), "mg"));
            nutrientsMap.put("K",          new RecipeApiResponse.Nutrient("Potassium", nutrientsInfo.getProtein(), "mg"));
            nutrientsMap.put("FE",         new RecipeApiResponse.Nutrient("Iron", nutrientsInfo.getProtein(), "mg"));
            nutrientsMap.put("ZN",         new RecipeApiResponse.Nutrient("Zinc", nutrientsInfo.getProtein(), "mg"));
            nutrientsMap.put("P",          new RecipeApiResponse.Nutrient("Phosphorus", nutrientsInfo.getProtein(), "mg"));
            nutrientsMap.put("VITA_RAE",   new RecipeApiResponse.Nutrient("Vitamin A", nutrientsInfo.getProtein(), "µg"));
            nutrientsMap.put("VITC",       new RecipeApiResponse.Nutrient("Vitamin C", nutrientsInfo.getProtein(), "mg"));
            nutrientsMap.put("THIA",       new RecipeApiResponse.Nutrient("Thiamin (B1)", nutrientsInfo.getProtein(), "mg"));
            nutrientsMap.put("RIBF",       new RecipeApiResponse.Nutrient("Riboflavin (B2)", nutrientsInfo.getProtein(), "mg"));
            nutrientsMap.put("NIA",        new RecipeApiResponse.Nutrient("Niacin (B3)", nutrientsInfo.getProtein(), "mg"));
            nutrientsMap.put("VITB6A",     new RecipeApiResponse.Nutrient("Vitamin B6", nutrientsInfo.getProtein(), "mg"));
            nutrientsMap.put("FOLDFE",     new RecipeApiResponse.Nutrient("Folate equivalent (total)", nutrientsInfo.getProtein(), "µg"));
            nutrientsMap.put("FOLFD",      new RecipeApiResponse.Nutrient("Folate (food)", nutrientsInfo.getProtein(), "µg"));
            nutrientsMap.put("FOLAC",      new RecipeApiResponse.Nutrient("Folic acid", nutrientsInfo.getProtein(), "µg"));
            nutrientsMap.put("VITB12",     new RecipeApiResponse.Nutrient("Vitamin B12", nutrientsInfo.getProtein(), "µg"));
            nutrientsMap.put("VITD",       new RecipeApiResponse.Nutrient("Vitamin D", nutrientsInfo.getProtein(), "µg"));
            nutrientsMap.put("TOCPHA",     new RecipeApiResponse.Nutrient("Vitamin E", nutrientsInfo.getProtein(), "mg"));
            nutrientsMap.put("VITK1",      new RecipeApiResponse.Nutrient("Vitamin K", nutrientsInfo.getProtein(), "mg"));
        }
        return nutrientsMap;
    }
}