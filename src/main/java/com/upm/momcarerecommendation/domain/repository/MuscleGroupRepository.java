package com.upm.momcarerecommendation.domain.repository;

import com.upm.momcarerecommendation.domain.entity.MuscleGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MuscleGroupRepository extends JpaRepository<MuscleGroup, Long> {
    boolean existsByName(String name);
}
