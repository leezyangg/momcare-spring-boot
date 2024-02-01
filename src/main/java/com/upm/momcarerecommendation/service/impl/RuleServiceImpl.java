package com.upm.momcarerecommendation.service.impl;

import com.upm.momcarerecommendation.domain.model.ExerciseRecommend;
import com.upm.momcarerecommendation.domain.model.FoodRecommend;
import com.upm.momcarerecommendation.domain.model.MotherRequest;
import com.upm.momcarerecommendation.domain.model.NutritionRecommend;
import com.upm.momcarerecommendation.service.RuleService;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RuleServiceImpl implements RuleService {
    private final KieContainer dietKieContainer;
    private final KieContainer exerciseKieContainer;
    public RuleServiceImpl(@Qualifier("dietKieContainer")KieContainer dietKieContainer, @Qualifier("exerciseKieContainer")KieContainer exerciseKieContainer) {
        this.dietKieContainer = dietKieContainer;
        this.exerciseKieContainer = exerciseKieContainer;
    }


    @Override
    public Map<String, List<String>> getRecipeParams(MotherRequest motherRequest) {

        KieSession kieSession = dietKieContainer.newKieSession();
        kieSession.insert(motherRequest);
        kieSession.fireAllRules();
        kieSession.dispose();

        Collection<?> nutritionRecommendation = kieSession.getObjects(o-> o.getClass() == NutritionRecommend.class);
        Collection<?> foodRecommendation = kieSession.getObjects(o-> o.getClass() == FoodRecommend.class);
        return extractRecipeParams(nutritionRecommendation, foodRecommendation);
    }

    @Override
    public Map<String, List<String>> getExerciseParams(MotherRequest motherRequest) {

        KieSession kieSession = exerciseKieContainer.newKieSession();
        kieSession.insert(motherRequest);
        kieSession.fireAllRules();
        kieSession.dispose();

        Collection<?> exerciseRecommendations = kieSession.getObjects(o -> o instanceof ExerciseRecommend);
        return extractExerciseParams(exerciseRecommendations);
    }



    // Helper method for extracting Object into hashMap format for query purpose
    private Map<String, List<String>> extractExerciseParams(Collection<?> exerciseRecommendations) {
        Map<String, List<String>> criteria = new HashMap<>();

        for (Object recommendation : exerciseRecommendations) {
            if (recommendation instanceof ExerciseRecommend recommend) {
                criteria.computeIfAbsent(recommend.getDatabaseField(), k -> new ArrayList<>())
                        .add(recommend.getQueryParam());
            }
        }
        return criteria;
    }

    private static Map<String, List<String>> extractRecipeParams(Collection<?> nutritionRecommendation, Collection<?> foodRecommendation) {
        Map<String, List<String>> recommendationMap = new HashMap<>();

        for (Object recommendation : nutritionRecommendation) {
            if (recommendation instanceof NutritionRecommend nutritionRecommend) {
                recommendationMap.computeIfAbsent(nutritionRecommend.getNutrition(), k -> new ArrayList<>())
                        .add(nutritionRecommend.getQuantity());
            }
        }

        for (Object recommendation : foodRecommendation) {
            if (recommendation instanceof FoodRecommend foodRecommend) {
                recommendationMap.computeIfAbsent(foodRecommend.getParameter(), k -> new ArrayList<>())
                        .add(foodRecommend.getValue());
            }
        }
        return recommendationMap;
    }
}
