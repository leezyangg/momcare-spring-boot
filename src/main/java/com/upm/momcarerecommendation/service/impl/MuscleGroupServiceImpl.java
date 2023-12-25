package com.upm.momcarerecommendation.service.impl;

import com.upm.momcarerecommendation.domain.entity.MuscleGroup;
import com.upm.momcarerecommendation.domain.repository.MuscleGroupRepository;
import com.upm.momcarerecommendation.service.MuscleGroupService;
import org.springframework.stereotype.Service;

@Service
public class MuscleGroupServiceImpl implements MuscleGroupService {
    private final MuscleGroupRepository muscleGroupRepository;

    public MuscleGroupServiceImpl(MuscleGroupRepository muscleGroupRepository) {
        this.muscleGroupRepository = muscleGroupRepository;
    }

    @Override
    public MuscleGroup saveMuscleGroup(MuscleGroup muscleGroup) {
        return muscleGroupRepository.save(muscleGroup);
    }
}
