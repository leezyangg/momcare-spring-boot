package com.upm.momcarerecommendation.domain.repository;

import com.upm.momcarerecommendation.domain.entity.NutrientsInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NutrientsInfoRepository extends JpaRepository<NutrientsInfo, Long> {
}
