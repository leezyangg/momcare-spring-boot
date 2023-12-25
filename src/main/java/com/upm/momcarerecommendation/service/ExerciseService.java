package com.upm.momcarerecommendation.service;

import com.upm.momcarerecommendation.domain.entity.Exercise;

import java.util.List;
import java.util.Map;

public interface ExerciseService {
    Exercise saveExercise(Exercise exercise);

    List<Exercise> findExerciseByCriteria(Map<String, List<String>> criteria);
}
