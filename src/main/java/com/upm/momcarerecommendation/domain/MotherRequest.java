package com.upm.momcarerecommendation.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MotherRequest {
    private String name;
    private int age;
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

    public record BloodPressure(int systolic, int diastolic) {}
}
