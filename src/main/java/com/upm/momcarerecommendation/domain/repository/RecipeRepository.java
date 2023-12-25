package com.upm.momcarerecommendation.domain.repository;

import com.upm.momcarerecommendation.domain.entity.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, Long>, RecipeRepositoryCustom {
    boolean existsByLabel(String label);
}
