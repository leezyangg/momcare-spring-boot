package com.upm.momcarerecommendation.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class NutrientsInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double fat;
    private Double carbs;
    private Double fiber;
    private Double protein;
    private Double sodium;
    private Double calcium;
    private Double magnesium;
    private Double iron;
    private Double zinc;
    private Double phosphorus;
    private Double vitaminA;
    private Double vitaminC;
    private Double vitaminB6;
    private Double folicAcid;
    private Double vitaminB12;
    private Double vitaminD;
    private Double vitaminE;
    private Double vitaminK;
}
