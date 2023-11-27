package com.upm.momcarerecommendation.service.impl;

import com.upm.momcarerecommendation.domain.FoodRecommend;
import com.upm.momcarerecommendation.domain.MotherRequest;
import com.upm.momcarerecommendation.domain.NutritionRecommend;
import com.upm.momcarerecommendation.service.RecommendationService;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final KieContainer kieContainer;

    public RecommendationServiceImpl(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }

    @Override
    public Map<String, String> generateFoodRecommendation(MotherRequest motherRequest) {
        KieSession kieSession = kieContainer.newKieSession();
        kieSession.insert(motherRequest);
        kieSession.fireAllRules();
        kieSession.dispose();
        Collection<?> nutritionRecommendation = kieSession.getObjects(o-> o.getClass() == NutritionRecommend.class);
        Collection<?> foodRecommendation = kieSession.getObjects(o-> o.getClass() == FoodRecommend.class);

        // add the loop for inserting the foodRecommend into the recommendationMap;
        return getStringStringMap(nutritionRecommendation, foodRecommendation);
    }

    private static Map<String, String> getStringStringMap(Collection<?> nutritionRecommendation, Collection<?> foodRecommendation) {
        Map<String, String> recommendationMap = new HashMap<>();
        for (Object recommendation  : nutritionRecommendation) {
            if (recommendation instanceof NutritionRecommend nutritionRecommend) {
                recommendationMap.put(
                        nutritionRecommend.getNutrition(), nutritionRecommend.getQuantity());
            }
        }

        for (Object recommendation  : foodRecommendation) {
            if (recommendation instanceof FoodRecommend foodRecommend) {
                recommendationMap.put(
                        foodRecommend.getParameter(), foodRecommend.getValue());
            }
        }
        return recommendationMap;
    }

}
