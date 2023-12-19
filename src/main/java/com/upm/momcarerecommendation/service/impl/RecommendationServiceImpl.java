package com.upm.momcarerecommendation.service.impl;

import com.upm.momcarerecommendation.domain.model.FoodRecommend;
import com.upm.momcarerecommendation.domain.model.MotherRequest;
import com.upm.momcarerecommendation.domain.model.NutritionRecommend;
import com.upm.momcarerecommendation.service.RecommendationService;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecommendationServiceImpl implements RecommendationService {
    private final KieContainer kieContainer;
    public RecommendationServiceImpl(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }


    @Override
    public Map<String, List<String>> generateFoodRecommendation(MotherRequest motherRequest) {

        KieSession kieSession = kieContainer.newKieSession();
        kieSession.insert(motherRequest);
        kieSession.fireAllRules();
        kieSession.dispose();
        System.out.println(motherRequest.getDetectedDiseases());

        Collection<?> nutritionRecommendation = kieSession.getObjects(o-> o.getClass() == NutritionRecommend.class);
        Collection<?> foodRecommendation = kieSession.getObjects(o-> o.getClass() == FoodRecommend.class);

        return getStringStringMap(nutritionRecommendation, foodRecommendation);
    }



    // loop for inserting the foodRecommend & nutritionRecommendation into the recommendationMap
    // this is to get them to serve as the param
    private static Map<String, List<String>> getStringStringMap(Collection<?> nutritionRecommendation, Collection<?> foodRecommendation) {
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
