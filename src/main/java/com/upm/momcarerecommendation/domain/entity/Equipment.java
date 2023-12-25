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
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "equipment")
    private List<Exercise> exercise;


    // Custom constructor
    public Equipment(String name) {
        this.name = name;
    }
}
