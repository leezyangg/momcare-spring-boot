package com.upm.momcarerecommendation.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Threshold {
    private String nutrition;
    private Double requiredAmount;
    private Double maximumAmount;
}
