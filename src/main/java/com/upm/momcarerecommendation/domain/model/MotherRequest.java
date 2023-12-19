package com.upm.momcarerecommendation.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MotherRequest {
    // mother parameter field
    private int age;
    private String ethnicity;
    private String dietReligious;
    private List<String> historicalDisease = new ArrayList<>();
    private List<String> allergyFood = new ArrayList<>();
    private BloodPressure bloodPressure;
    private double bloodSugar;
    private double haemoglobinLevel;
    private boolean isPregnant;
    private int trimester;
    private boolean isBreastFeeding;
    private String deliveryMethod;
    // TODO: If relevant, consider adding more detailed health information in MotherRequest,
    //  like specific health goals or conditions (for my case might be goal to increase or decrease weight)

    // food intake by mother to calculate totalDailyIntake
    private TotalDailyIntake totalDailyIntake;

    private Set<String> detectedDiseases = new HashSet<>();


    public record BloodPressure(int systolic, int diastolic) {}
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TotalDailyIntake {
        private double totalCalories;
        private double totalProteins;
        private double totalCarbs;
        private double totalFats;
        private double totalFolicAcid;
        // TODO: add more fields for other nutrients if needed
    }
}
