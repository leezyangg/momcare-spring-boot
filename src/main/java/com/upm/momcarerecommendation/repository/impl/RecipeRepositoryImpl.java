package com.upm.momcarerecommendation.repository.impl;

import com.upm.momcarerecommendation.domain.entity.RecipeEntity;
import com.upm.momcarerecommendation.domain.model.Range;
import com.upm.momcarerecommendation.repository.RecipeRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeRepositoryImpl implements RecipeRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<RecipeEntity> findRecipesByCriteria(Map<String, Object> criteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<RecipeEntity> query = criteriaBuilder.createQuery(RecipeEntity.class);
        Root<RecipeEntity> recipe = query.from(RecipeEntity.class);
        List<Predicate> predicates = new ArrayList<>();


        // dynamic add predicates based on criteria passed in
        if (criteria.containsKey("healthLabels")) {
            List<String> healthLabels = ((List<String>) criteria.get("healthLabels"))
                    .stream()
                    .map(this::mapDroolsLabelToDBField)
                    .toList();

            Predicate healthLabelsPredicate = criteriaBuilder.conjunction();
            for (String label : healthLabels) {
                healthLabelsPredicate = criteriaBuilder.and(healthLabelsPredicate, criteriaBuilder.isMember(label, recipe.get("healthLabels")));
            }
            predicates.add(healthLabelsPredicate);
        }

        if (criteria.containsKey("dietLabels")) {
            List<String> dietLabels = ((List<String>) criteria.get("dietLabels"))
                    .stream()
                    .map(this::mapDroolsLabelToDBField)
                    .toList();

            Predicate dietLabelsPredicate = criteriaBuilder.conjunction();
            for (String label : dietLabels) {
                dietLabelsPredicate = criteriaBuilder.and(dietLabelsPredicate, criteriaBuilder.isMember(label, recipe.get("dietLabels")));
            }
            predicates.add(dietLabelsPredicate);
        }

        addIngredientExclusionPredicates(criteria, criteriaBuilder, recipe, predicates);        // having minor bug --> we could only exclude the recipe having exactly thing we specified
        addNutritionalPredicates(criteria, criteriaBuilder, recipe, predicates);

        query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

        // Create the query and set the maximum number of results
        TypedQuery<RecipeEntity> typedQuery = entityManager.createQuery(query);
        typedQuery.setMaxResults(10);

        return typedQuery.getResultList();
    }


    // adding nutrition range field query
    private void addNutritionalPredicates(Map<String, Object> criteria, CriteriaBuilder criteriaBuilder, Root<RecipeEntity> recipe, List<Predicate> predicates) {
        criteria.forEach((key, value) -> {
            if (key.startsWith("nutrients[")) {
                String nutrientField = mapNutrientKeyToDBField(key);
                if (nutrientField != null && value instanceof List<?> valueList) {
                    for (Object item : valueList) {
                        if (item instanceof Range range) {
                            if (range.getMin() != null && range.getMax() != null) {
                                predicates.add(criteriaBuilder.between(recipe.get("totalNutrients").get(nutrientField), range.getMin(), range.getMax()));
                            } else if (range.getMin() != null) {
                                predicates.add(criteriaBuilder.ge(recipe.get("totalNutrients").get(nutrientField), range.getMin()));
                            } else if (range.getMax() != null) {
                                predicates.add(criteriaBuilder.le(recipe.get("totalNutrients").get(nutrientField), range.getMax()));
                            }
                        }
                    }
                }
            }
        });
    }

    private void addIngredientExclusionPredicates(Map<String, Object> criteria, CriteriaBuilder criteriaBuilder, Root<RecipeEntity> recipe, List<Predicate> predicates) {
        if (criteria.containsKey("ingredientLines")) {
            List<String> excludedIngredients = (List<String>) criteria.get("ingredientLines");
            for (String ingredient : excludedIngredients) {
                predicates.add(criteriaBuilder.isNotMember(ingredient, recipe.get("ingredientLines")));
            }
        }
    }

    private String mapNutrientKeyToDBField(String nutrientKey) {
        Map<String, String> nutrientFieldMapping = new HashMap<>();
        nutrientFieldMapping.put("nutrients[FAT]", "fat");
        nutrientFieldMapping.put("nutrients[CHOCDF]", "carbs");
        nutrientFieldMapping.put("nutrients[FIBTG]", "fiber");
        nutrientFieldMapping.put("nutrients[PROCNT]", "protein");
        nutrientFieldMapping.put("nutrients[NA]", "sodium");
        nutrientFieldMapping.put("nutrients[CA]", "calcium");
        nutrientFieldMapping.put("nutrients[MG]", "magnesium");
        nutrientFieldMapping.put("nutrients[FE]", "iron");
        nutrientFieldMapping.put("nutrients[ZN]", "zinc");
        nutrientFieldMapping.put("nutrients[P]", "phosphorus");
        nutrientFieldMapping.put("nutrients[VITA_RAE]", "vitaminA");
        nutrientFieldMapping.put("nutrients[VITC]", "vitaminC");
        nutrientFieldMapping.put("nutrients[VITB6A]", "vitaminB6");
        nutrientFieldMapping.put("nutrients[FOLAC]", "folicAcid");
        nutrientFieldMapping.put("nutrients[VITB12]", "vitaminB12");
        nutrientFieldMapping.put("nutrients[VITD]", "vitaminD");
        nutrientFieldMapping.put("nutrients[TOCPHA]", "vitaminE");
        nutrientFieldMapping.put("nutrients[VITK1]", "vitaminK");

        return nutrientFieldMapping.get(nutrientKey);
    }

    // TODO: add more mapping here
    private String mapDroolsLabelToDBField(String droolsLabel) {
        Map<String, String> labelMappings = new HashMap<>();
        labelMappings.put("low-potassium", "Low Potassium");
        labelMappings.put("vegetarian", "Vegetarian");
        labelMappings.put("soy-free", "Soy-Free");
        labelMappings.put("low-sodium", "Low-Sodium");
        labelMappings.put("low-carb", "Low-Carb");
        labelMappings.put("pork-free", "Pork-Free");

        return labelMappings.getOrDefault(droolsLabel, droolsLabel);
    }
}
