package com.upm.momcarerecommendation.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeApiResponse {
    private List<Hit> hits;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Hit {
        private Recipe recipe;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Recipe {
        private String label;
        private String image;
        private String source;
        private String url;
        private double yield;
        private List<String> dietLabels;
        private List<String> healthLabels;
        private List<String> ingredientLines;
        private List<String> instructionLines;
        private double calories;
        private double totalTime;
        private Map<String, Nutrient> totalNutrients;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Nutrient {
        private String label;
        private double quantity;
        private String unit;
    }
}

