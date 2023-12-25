package com.upm.momcarerecommendation.domain.repository.impl;

import com.upm.momcarerecommendation.domain.entity.RecipeEntity;
import com.upm.momcarerecommendation.domain.model.Range;
import com.upm.momcarerecommendation.domain.repository.RecipeRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeRepositoryCustomImpl implements RecipeRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<RecipeEntity> findRecipesByCriteria(Map<String, Object> criteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<RecipeEntity> query = criteriaBuilder.createQuery(RecipeEntity.class);
        Root<RecipeEntity> recipe = query.from(RecipeEntity.class);
        List<Predicate> predicates = new ArrayList<>();


        // add healthLabels Predicates
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

        // add dietLabels Predicates
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
        addCaloriesPredicates(criteria, criteriaBuilder, recipe, predicates);
        addNutritionalPredicates(criteria, criteriaBuilder, recipe, predicates);

        query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

        // Create the query and set the maximum number of results
        TypedQuery<RecipeEntity> typedQuery = entityManager.createQuery(query);
        typedQuery.setMaxResults(10);

        return typedQuery.getResultList();
    }



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

    private void addCaloriesPredicates(Map<String, Object> criteria, CriteriaBuilder criteriaBuilder, Root<RecipeEntity> recipe, List<Predicate> predicates) {
        if (criteria.containsKey("calories")) {
            Object value = criteria.get("calories");
            if (value instanceof List) {
                List<Range> ranges = (List<Range>) value;

                Expression<Double> calories = recipe.get("calories");
                Expression<Double> yield = recipe.get("yield");
                Expression<Number> caloriesPerYield = criteriaBuilder.quot(calories, yield);
                Expression<Double> caloriesPerYieldDouble = caloriesPerYield.as(Double.class);

                for (Range range : ranges) {
                    if (range.getMin() != null && range.getMax() != null) {
                        predicates.add(criteriaBuilder.between(caloriesPerYieldDouble, range.getMin(), range.getMax()));
                    } else if (range.getMin() != null) {
                        predicates.add(criteriaBuilder.ge(caloriesPerYieldDouble, range.getMin()));
                    } else if (range.getMax() != null) {
                        predicates.add(criteriaBuilder.le(caloriesPerYieldDouble, range.getMax()));
                    }
                }
            }

        }
    }

    private void addIngredientExclusionPredicates(Map<String, Object> criteria, CriteriaBuilder criteriaBuilder, Root<RecipeEntity> recipe, List<Predicate> predicates) {
        if (criteria.containsKey("ingredientLines")) {
            List<String> excludedIngredients = (List<String>) criteria.get("ingredientLines");
            for (String ingredientToBeExcluded : excludedIngredients) {
                predicates.add(criteriaBuilder.isNotMember(ingredientToBeExcluded, recipe.get("ingredientLines")));
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
        nutrientFieldMapping.put("calories", "calories");

        return nutrientFieldMapping.get(nutrientKey);
    }

    // TODO: add more mapping here
    private String mapDroolsLabelToDBField(String droolsLabel) {
        Map<String, String> labelMappings = new HashMap<>();

        labelMappings.put("vegetarian", "Vegetarian");
        labelMappings.put("vegan", "Vegan");
        labelMappings.put("pecatarian", "Pescatarian");
        labelMappings.put("kosher", "Kosher");

        labelMappings.put("soy-free", "Soy-Free");
        labelMappings.put("wheat-free", "Wheat-Free");
        labelMappings.put("dairy-free", "Dairy-Free");
        labelMappings.put("pork-free", "Pork-Free");
        labelMappings.put("egg-free", "Egg-Free");
        labelMappings.put("fish-free", "Fish-Free");
        labelMappings.put("shellfish-free", "Shellfish-Free");
        labelMappings.put("red-meat-free", "Red-Meat-Free");
        labelMappings.put("peanut-free", "Peanut-Free");
        labelMappings.put("tree-nut-free", "Tree-Nut-Free");

        labelMappings.put("low-sodium", "Low-Sodium");
        labelMappings.put("low-carb", "Low-Carb");
        labelMappings.put("low-sugar", "Low-Sugar");
        labelMappings.put("low-potassium", "Low Potassium");

        return labelMappings.getOrDefault(droolsLabel, droolsLabel);
    }
}
