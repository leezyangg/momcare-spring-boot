package com.upm.momcarerecommendation.mapper.impl;

import com.upm.momcarerecommendation.domain.dto.ExerciseDto;
import com.upm.momcarerecommendation.domain.entity.Equipment;
import com.upm.momcarerecommendation.domain.entity.Exercise;
import com.upm.momcarerecommendation.domain.entity.MuscleGroup;
import com.upm.momcarerecommendation.mapper.Mapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ExerciseMapper implements Mapper<ExerciseDto, Exercise> {
    @Override
    public Exercise mapToEntity(ExerciseDto exerciseDto) {
        return Exercise.builder()
                .name(exerciseDto.getName())
                .description(exerciseDto.getDescription())
                .image(exerciseDto.getImage())
                .duration(exerciseDto.getDuration())
                .metValue(exerciseDto.getMetValue())
                .suitableForPregnancyTrimester(exerciseDto.getSuitableForPregnancyTrimester())
                .safeAfterCSectionWeek(exerciseDto.getSafeAfterCSectionWeek())
                .build();
    }
    @Override
    public ExerciseDto mapToDto(Exercise exercise) {
        return ExerciseDto.builder()
                .name(exercise.getName())
                .description(exercise.getDescription())
                .image(exercise.getImage())
                .duration(exercise.getDuration())
                .metValue(exercise.getMetValue())
                .suitableForPregnancyTrimester(exercise.getSuitableForPregnancyTrimester())
                .safeAfterCSectionWeek(exercise.getSafeAfterCSectionWeek())
                .activityType(exercise.getActivityType().name())
                .muscleGroups(exercise.getMuscleGroups().stream().map(MuscleGroup::getName).collect(Collectors.toList()))
                .equipment(exercise.getEquipment().stream().map(Equipment::getName).collect(Collectors.toList()))
                .build();
    }
}