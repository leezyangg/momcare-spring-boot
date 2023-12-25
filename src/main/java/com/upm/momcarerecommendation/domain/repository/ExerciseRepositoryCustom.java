package com.upm.momcarerecommendation.domain.repository;

import com.upm.momcarerecommendation.domain.entity.Exercise;

import java.util.List;
import java.util.Map;

public interface ExerciseRepositoryCustom {
    List<Exercise> findExercisesBasedOnCriteria(Map<String, List<String>> criteria);
}
