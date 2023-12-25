package com.upm.momcarerecommendation.dataloader;

import com.upm.momcarerecommendation.domain.entity.Equipment;
import com.upm.momcarerecommendation.domain.entity.Exercise;
import com.upm.momcarerecommendation.domain.entity.MuscleGroup;
import com.upm.momcarerecommendation.domain.repository.EquipmentRepository;
import com.upm.momcarerecommendation.domain.repository.ExerciseRepository;
import com.upm.momcarerecommendation.domain.repository.MuscleGroupRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Order(3)
public class ExerciseDataLoader implements CommandLineRunner {
    private final ExerciseRepository exerciseRepository;
    private final EquipmentRepository equipmentRepository;
    private final MuscleGroupRepository muscleGroupRepository;
    public ExerciseDataLoader(ExerciseRepository exerciseRepository, EquipmentRepository equipmentRepository, MuscleGroupRepository muscleGroupRepository) {
        this.exerciseRepository = exerciseRepository;
        this.equipmentRepository = equipmentRepository;
        this.muscleGroupRepository = muscleGroupRepository;
    }

    @Override
    public void run(String... args) {
        loadExerciseData();
    }

    private void loadExerciseData() {

        if (!exerciseRepository.existsByName("Stationary Cycling")) {
            List<MuscleGroup> muscleGroups = muscleGroupRepository.findAllById(Arrays.asList(9L, 10L, 11L, 12L));
            List<Equipment> equipment = equipmentRepository.findAllById(List.of(3L));
            Exercise stationaryCycling = Exercise.builder()
                    .name("Stationary Cycling")
                    .description("A stationary bike workout is a low-impact exercise that uses smooth movements to " +
                                 "strengthen bones and joints. It's also a great form of cardio exercise that can help improve heart health and lower blood pressure.")
                    .image("stationary-cycling.jpg")
                    .duration(10)
                    .metValue(6.8)
                    .suitableForPregnancyTrimester("All Trimesters")
                    .suitableForPostpartum(true)
                    .safeAfterCSectionWeek(3)
                    .activityType(Exercise.ActivityType.CARDIO)
                    .intensityLevel(Exercise.IntensityLevel.MODERATE)
                    .impactLevel(Exercise.ImpactLevel.LOW)
                    .muscleGroups(muscleGroups)
                    .equipment(equipment)
                    .build();
            exerciseRepository.save(stationaryCycling);
        }

        if (!exerciseRepository.existsByName("Walking")) {
            List<MuscleGroup> muscleGroups = muscleGroupRepository.findAllById(Arrays.asList(9L, 10L, 11L, 12L));
            Exercise stationaryCycling = Exercise.builder()
                    .name("Walking")
                    .description("Walking is a great workout that doesn't strain your joints and muscles. " +
                                 "It can help keep your back muscles strong and warm up so that they can support your growing belly and not become stiff.")
                    .duration(20)
                    .image("walking.jpeg")
                    .metValue(2.8)
                    .suitableForPregnancyTrimester("All Trimesters")
                    .suitableForPostpartum(true)
                    .safeAfterCSectionWeek(0)
                    .activityType(Exercise.ActivityType.CARDIO)
                    .intensityLevel(Exercise.IntensityLevel.LOW)
                    .impactLevel(Exercise.ImpactLevel.LOW)
                    .muscleGroups(muscleGroups)
                    .build();
            exerciseRepository.save(stationaryCycling);
        }

        if (!exerciseRepository.existsByName("Brisk Walking")) {
            List<MuscleGroup> muscleGroups = muscleGroupRepository.findAllById(Arrays.asList(9L, 10L, 11L, 12L));
            Exercise stationaryCycling = Exercise.builder()
                    .name("Brisk Walking")
                    .description("Brisk walking is a walking pace that's driven by your metabolic rate and is " +
                                  "considered beneficial for cardiovascular health and overall fitness.")
                    .duration(20)
                    .image("brisk-walking.jpg")
                    .metValue(4.3)
                    .suitableForPregnancyTrimester("All Trimesters")
                    .suitableForPostpartum(true)
                    .safeAfterCSectionWeek(3)
                    .activityType(Exercise.ActivityType.CARDIO)
                    .intensityLevel(Exercise.IntensityLevel.MODERATE)
                    .impactLevel(Exercise.ImpactLevel.LOW)
                    .muscleGroups(muscleGroups)
                    .build();
            exerciseRepository.save(stationaryCycling);
        }

        if (!exerciseRepository.existsByName("Swimming")) {
            List<MuscleGroup> muscleGroups = muscleGroupRepository.findAllById(List.of(14L));
            Exercise stationaryCycling = Exercise.builder()
                    .name("Swimming")
                    .description("It's low impact and gentle on your body. The water keeps you from overheating and " +
                                 "prevents injury by supporting your joints and ligaments as you exercise, which is " +
                                 "especially helpful for moms-to-be with round ligament pain.")
                    .duration(20)
                    .image("swimming.jpg")
                    .metValue(6.0)
                    .suitableForPregnancyTrimester("All Trimesters")
                    .suitableForPostpartum(true)
                    .safeAfterCSectionWeek(3)
                    .activityType(Exercise.ActivityType.CARDIO)
                    .intensityLevel(Exercise.IntensityLevel.MODERATE)
                    .impactLevel(Exercise.ImpactLevel.LOW)
                    .muscleGroups(muscleGroups)
                    .build();
            exerciseRepository.save(stationaryCycling);
        }


    }
}
