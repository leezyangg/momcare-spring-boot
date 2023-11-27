package com.upm.momcarerecommendation.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetectedDisease {
    private String diseaseName;
    private String severity;
    private MotherRequest mother;

    public DetectedDisease(String diseaseName, MotherRequest mother) {
        this.diseaseName = diseaseName;
        this.mother = mother;
    }
}
