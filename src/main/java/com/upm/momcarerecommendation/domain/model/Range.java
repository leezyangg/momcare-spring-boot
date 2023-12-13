package com.upm.momcarerecommendation.domain.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Range {
    private Double min;
    private Double max;
}
