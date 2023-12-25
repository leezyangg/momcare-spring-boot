package com.upm.momcarerecommendation.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseDto {

    private String name;
    private String description;
    private String image;
    private Integer duration;
    private Double metValue;
    private String suitableForPregnancyTrimester;
    private Integer safeAfterCSectionWeek;
    private String activityType;
    private List<String> muscleGroups;
    private List<String> equipment;
}
