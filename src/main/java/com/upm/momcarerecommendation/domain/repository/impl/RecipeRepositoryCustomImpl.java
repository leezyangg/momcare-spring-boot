package com.upm.momcarerecommendation.domain.repository.impl;

import com.upm.momcarerecommendation.domain.entity.RecipeEntity;
import com.upm.momcarerecommendation.domain.model.Range;
import com.upm.momcarerecommendation.domain.repository.RecipeRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

import java.util.*;

public class RecipeRepositoryCustomImpl implements RecipeRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<RecipeEntity> findRecipesByCriteria(Map<String, Object> criteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<RecipeEntity> query = criteriaBuilder.createQuery(RecipeEntity.class);
        Root<RecipeEntity> recipe = query.from(RecipeEntity.class);
        List<Predicate> predicates = new ArrayList<>();

        // Adding Predicates
        addHealthLabelPredicates(criteria, criteriaBuilder, recipe, predicates);
        addDietLabelPredicates(criteria, criteriaBuilder, recipe, predicates);
        addIngredientExclusionPredicates(criteria, criteriaBuilder, recipe, predicates);        // having minor bug --> we could only exclude the recipe having exactly thing we specified
        addCaloriesPredicates(criteria, criteriaBuilder, recipe, predicates);
        addNutritionalPredicates(criteria, criteriaBuilder, recipe, predicates);

        query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        TypedQuery<RecipeEntity> typedQuery = entityManager.createQuery(query);
        typedQuery.setMaxResults(10);
        return typedQuery.getResultList();
    }


    private void addHealthLabelPredicates(Map<String, Object> criteria, CriteriaBuilder criteriaBuilder, Root<RecipeEntity> recipe, List<Predicate> predicates) {
        if (criteria.containsKey("healthLabels")) {
            Object rawHealthLabels = criteria.get("healthLabels");
            if (rawHealthLabels instanceof List<?> rawList) {
                if (!rawList.isEmpty() && rawList.get(0) instanceof String) {
                    List<String> healthLabels = rawList.stream()
                            .map(Object::toString)
                            .map(this::mapDroolsLabelToDBField)
                            .toList();

                    Predicate healthLabelsPredicate = criteriaBuilder.conjunction();
                    for (String label : healthLabels) {
                        healthLabelsPredicate = criteriaBuilder.and(healthLabelsPredicate, criteriaBuilder.isMember(label, recipe.get("healthLabels")));
                    }
                    predicates.add(healthLabelsPredicate);
                }
            }
        }
    }

    private void addDietLabelPredicates(Map<String, Object> criteria, CriteriaBuilder criteriaBuilder, Root<RecipeEntity> recipe, List<Predicate> predicates) {
        if (criteria.containsKey("dietLabels")) {
            Object rawDietLabels = criteria.get("dietLabels");
            if (rawDietLabels instanceof List<?> rawList) {
                if (!rawList.isEmpty() && rawList.get(0) instanceof String) {
                    List<String> dietLabels = rawList.stream()
                            .map(Object::toString)
                            .map(this::mapDroolsLabelToDBField)
                            .toList();

                    Predicate dietLabelsPredicate = criteriaBuilder.conjunction();
                    for (String label : dietLabels) {
                        dietLabelsPredicate = criteriaBuilder.and(dietLabelsPredicate, criteriaBuilder.isMember(label, recipe.get("dietLabels")));
                    }
                    predicates.add(dietLabelsPredicate);
                }
            }
        }
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
            Object rawCaloriesValue = criteria.get("calories");
            if (rawCaloriesValue instanceof List<?> rawList) {
                if (!rawList.isEmpty() && rawList.get(0) instanceof Range) {
                    List<Range> ranges = rawList.stream()
                            .filter(Range.class::isInstance)
                            .map(Range.class::cast)
                            .toList();

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
    }

    private void addIngredientExclusionPredicates(Map<String, Object> criteria, CriteriaBuilder criteriaBuilder, Root<RecipeEntity> recipe, List<Predicate> predicates) {
        if (criteria.containsKey("ingredientLines")) {
            Object rawExcludedIngredients = criteria.get("ingredientLines");
            if (rawExcludedIngredients instanceof List<?> rawList) {
                if (!rawList.isEmpty() && rawList.get(0) instanceof String) {
                    List<String> excludedIngredients = rawList.stream()
                            .map(Object::toString)
                            .toList();

                    for (String ingredientToBeExcluded : excludedIngredients) {
                        predicates.add(criteriaBuilder.isNotMember(ingredientToBeExcluded, recipe.get("ingredientLines")));
                    }
                }
            }
        }
    }



    private static final Map<String, String> NUTRIENT_FIELD_MAPPING;
    private static final Map<String, String> LABEL_MAPPINGS;

    static {
        Map<String, String> tempMap;
        NUTRIENT_FIELD_MAPPING = Map.ofEntries(Map.entry("nutrients[FAT]", "fat"), Map.entry("nutrients[CHOCDF]", "carbs"), Map.entry("vegetarian", "Vegetarian"), Map.entry("vegan", "Vegan"), Map.entry("nutrients[FIBTG]", "fiber"), Map.entry("nutrients[PROCNT]", "protein"), Map.entry("nutrients[NA]", "sodium"), Map.entry("nutrients[CA]", "calcium"), Map.entry("nutrients[MG]", "magnesium"), Map.entry("nutrients[FE]", "iron"), Map.entry("nutrients[ZN]", "zinc"), Map.entry("nutrients[P]", "phosphorus"), Map.entry("nutrients[VITA_RAE]", "vitaminA"), Map.entry("nutrients[VITC]", "vitaminC"), Map.entry("nutrients[VITB6A]", "vitaminB6"), Map.entry("nutrients[FOLAC]", "folicAcid"), Map.entry("nutrients[VITB12]", "vitaminB12"), Map.entry("nutrients[VITD]", "vitaminD"), Map.entry("nutrients[TOCPHA]", "vitaminE"), Map.entry("nutrients[VITK1]", "vitaminK"));

        tempMap = new HashMap<>();
        tempMap.put("vegetarian", "Vegetarian");
        tempMap.put("vegan", "Vegan");
        tempMap.put("pecatarian", "Pescatarian");
        tempMap.put("kosher", "Kosher");
        tempMap.put("soy-free", "Soy-Free");
        tempMap.put("wheat-free", "Wheat-Free");
        tempMap.put("dairy-free", "Dairy-Free");
        tempMap.put("pork-free", "Pork-Free");
        tempMap.put("egg-free", "Egg-Free");
        tempMap.put("fish-free", "Fish-Free");
        tempMap.put("shellfish-free", "Shellfish-Free");
        tempMap.put("red-meat-free", "Red-Meat-Free");
        tempMap.put("peanut-free", "Peanut-Free");
        tempMap.put("tree-nut-free", "Tree-Nut-Free");
        tempMap.put("low-sodium", "Low-Sodium");
        tempMap.put("low-carb", "Low-Carb");
        tempMap.put("low-sugar", "Low-Sugar");
        tempMap.put("low-potassium", "Low Potassium");
        LABEL_MAPPINGS = Collections.unmodifiableMap(tempMap);
    }

    private String mapNutrientKeyToDBField(String nutrientKey) {
        return NUTRIENT_FIELD_MAPPING.getOrDefault(nutrientKey, nutrientKey);
    }

    private String mapDroolsLabelToDBField(String droolsLabel) {
        return LABEL_MAPPINGS.getOrDefault(droolsLabel, droolsLabel);
    }
}
