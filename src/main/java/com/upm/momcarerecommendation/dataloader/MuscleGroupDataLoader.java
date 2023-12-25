package com.upm.momcarerecommendation.dataloader;

import com.upm.momcarerecommendation.domain.entity.MuscleGroup;
import com.upm.momcarerecommendation.domain.repository.MuscleGroupRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class MuscleGroupDataLoader implements CommandLineRunner {
    private final MuscleGroupRepository muscleGroupRepository;
    public MuscleGroupDataLoader(MuscleGroupRepository muscleGroupRepository) {
        this.muscleGroupRepository = muscleGroupRepository;
    }


    @Override
    public void run(String... args) {
        addMuscleGroupIfNotFound("Abs");
        addMuscleGroupIfNotFound("Legs");
        addMuscleGroupIfNotFound("Chest");
        addMuscleGroupIfNotFound("Back");
        addMuscleGroupIfNotFound("Shoulders");
        addMuscleGroupIfNotFound("Biceps");
        addMuscleGroupIfNotFound("Triceps");
        addMuscleGroupIfNotFound("Core");
        addMuscleGroupIfNotFound("Glutes");
        addMuscleGroupIfNotFound("Quadriceps");
        addMuscleGroupIfNotFound("Hamstrings");
        addMuscleGroupIfNotFound("Calves");
        addMuscleGroupIfNotFound("Forearms");
        addMuscleGroupIfNotFound("Full body");
        // TODO: add more muscle groups...
    }

    private void addMuscleGroupIfNotFound(String name) {
        if (!muscleGroupRepository.existsByName(name)) {
            muscleGroupRepository.save(new MuscleGroup(name));
        }
    }
}
