package com.upm.momcarerecommendation.domain.repository;

import com.upm.momcarerecommendation.domain.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    boolean existsByName(String name);
}
