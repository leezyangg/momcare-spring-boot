package com.upm.momcarerecommendation.domain.repository;

import com.upm.momcarerecommendation.domain.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long>, ExerciseRepositoryCustom {
    boolean existsByName(String name);
}
