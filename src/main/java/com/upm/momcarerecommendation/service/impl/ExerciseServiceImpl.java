package com.upm.momcarerecommendation.service.impl;

import com.upm.momcarerecommendation.domain.entity.Exercise;
import com.upm.momcarerecommendation.domain.repository.ExerciseRepository;
import com.upm.momcarerecommendation.service.ExerciseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ExerciseServiceImpl implements ExerciseService {
    private final ExerciseRepository exerciseRepository;
    public ExerciseServiceImpl(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }


    @Override
    public Exercise saveExercise(Exercise exercise) {
        return exerciseRepository.save(exercise);
    }

    @Override
    public List<Exercise> findExerciseByCriteria(Map<String, List<String>> criteria) {
        // dynamic query local database using JPA Criteria API
        return exerciseRepository.findExercisesBasedOnCriteria(criteria);
    }
}
