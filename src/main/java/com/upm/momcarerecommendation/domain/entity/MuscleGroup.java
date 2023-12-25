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
public class MuscleGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "muscleGroups")
    private List<Exercise> exercise;

    public MuscleGroup(String name) {
        this.name = name;
    }
}
