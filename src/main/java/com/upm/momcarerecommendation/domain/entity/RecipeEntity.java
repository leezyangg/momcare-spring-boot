package com.upm.momcarerecommendation.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class RecipeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String label;
    @Column(columnDefinition = "Text")
    private String image;
    private String source;
    private String url;
    private Double yield;

    @ElementCollection
    private List<String> dietLabels;

    @ElementCollection
    private List<String> healthLabels;

    @ElementCollection
    private List<String> cautions;

    @ElementCollection
    private List<String> ingredientLines;

    private Double calories;
    private Double totalTime;

    @OneToOne(cascade = CascadeType.ALL)
    private NutrientsInfo totalNutrients;

    @ElementCollection
    private List<String> instructionLines;
}
