package com.upm.momcarerecommendation.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(length=1000)
    private String description;
    private String image;
    private Integer duration;
    private Double metValue;    // Calories burned= MET value of the activity×body weight in kg×duration in hours
    private String suitableForPregnancyTrimester;
    private Boolean suitableForPostpartum;
    private Integer safeAfterCSectionWeek;

    @Enumerated(EnumType.STRING)
    private ActivityType activityType;

    @Enumerated(EnumType.STRING)
    private IntensityLevel intensityLevel;

    @Enumerated(EnumType.STRING)
    private ImpactLevel impactLevel;

    @ManyToMany
    @JoinTable(name = "exercise_muscle_group", joinColumns = @JoinColumn(name = "exercise_id"), inverseJoinColumns = @JoinColumn(name = "muscle_group_id"))
    private List<MuscleGroup> muscleGroups;

    @ManyToMany
    @JoinTable(name = "exercise_equipment", joinColumns = @JoinColumn(name = "exercise_id"), inverseJoinColumns = @JoinColumn(name = "equipment_id"))
    private List<Equipment> equipment;


    public enum ActivityType {
        STRENGTH, PILATES, CARDIO, AEROBIC
    }
    public enum IntensityLevel {
        LOW, MODERATE, HIGH
    }
    public enum ImpactLevel {
        LOW, MODERATE, HIGH
    }
}
