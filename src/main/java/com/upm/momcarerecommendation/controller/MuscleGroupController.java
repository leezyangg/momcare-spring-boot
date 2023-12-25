package com.upm.momcarerecommendation.controller;

import com.upm.momcarerecommendation.domain.entity.MuscleGroup;
import com.upm.momcarerecommendation.service.MuscleGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/muscleGroups")
public class MuscleGroupController {
    private final MuscleGroupService muscleGroupService;

    public MuscleGroupController(MuscleGroupService muscleGroupService) {
        this.muscleGroupService = muscleGroupService;
    }

    @PostMapping
    public ResponseEntity<MuscleGroup> addMuscleGroup(@RequestBody MuscleGroup muscleGroup) {
        MuscleGroup savedMuscleGroup = muscleGroupService.saveMuscleGroup(muscleGroup);
        return ResponseEntity.ok(savedMuscleGroup);
    }

}
