package com.upm.momcarerecommendation.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NutritionRecommend {
    private String nutrition;
    private String quantity;
    private MotherRequest mother;
}
