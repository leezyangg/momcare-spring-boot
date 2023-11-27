package com.upm.momcarerecommendation.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodRecommend {
    private String parameter;
    private String value;
    private MotherRequest mother;
}
