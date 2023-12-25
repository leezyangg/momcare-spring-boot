package com.upm.momcarerecommendation.service.impl;

import com.upm.momcarerecommendation.domain.dto.ExerciseDto;
import com.upm.momcarerecommendation.domain.entity.Exercise;
import com.upm.momcarerecommendation.domain.model.MotherRequest;
import com.upm.momcarerecommendation.mapper.Mapper;
import com.upm.momcarerecommendation.service.ExerciseRecommendationService;
import com.upm.momcarerecommendation.service.ExerciseService;
import com.upm.momcarerecommendation.service.RuleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ExerciseRecommendationServiceImpl implements ExerciseRecommendationService {
    private final RuleService ruleService;
    private final ExerciseService exerciseService;
    private final Mapper<ExerciseDto, Exercise> exerciseMapper;
    public ExerciseRecommendationServiceImpl(RuleService ruleService, ExerciseService exerciseService, Mapper<ExerciseDto, Exercise> exerciseMapper) {
        this.ruleService = ruleService;
        this.exerciseService = exerciseService;
        this.exerciseMapper = exerciseMapper;
    }

    @Override
    public List<ExerciseDto> getExerciseRecommendations(MotherRequest motherRequest) {
        Map<String, List<String>> exerciseQueryParam = ruleService.getExerciseParams(motherRequest);
        System.out.println(exerciseQueryParam);
        return exerciseService
                .findExerciseByCriteria(exerciseQueryParam)
                .stream()
                .map(exerciseMapper::mapToDto)
                .toList();
    }
}