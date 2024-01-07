package com.upm.momcarerecommendation.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "recipe")
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
    @CollectionTable(name = "recipe_diet_labels", joinColumns = @JoinColumn(name = "recipe_id"))
    private List<String> dietLabels;

    @ElementCollection
    @CollectionTable(name = "recipe_health_labels", joinColumns = @JoinColumn(name = "recipe_id"))
    private List<String> healthLabels;

    @ElementCollection
    @CollectionTable(name = "recipe_cautions", joinColumns = @JoinColumn(name = "recipe_id"))
    private List<String> cautions;

    @ElementCollection
    @CollectionTable(name = "recipe_ingredient_lines", joinColumns = @JoinColumn(name = "recipe_id"))
    private List<String> ingredientLines;

    private Double calories;
    private Double totalTime;

    @OneToOne(cascade = CascadeType.ALL)
    private NutrientsInfo totalNutrients;

    @ElementCollection
    @Column(columnDefinition = "Text")
    @CollectionTable(name = "recipe_instruction_lines", joinColumns = @JoinColumn(name = "recipe_id"))
    private List<String> instructionLines;
}
