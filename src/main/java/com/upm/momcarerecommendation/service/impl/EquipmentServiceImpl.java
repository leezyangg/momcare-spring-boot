package com.upm.momcarerecommendation.service.impl;

import com.upm.momcarerecommendation.domain.entity.Equipment;
import com.upm.momcarerecommendation.domain.repository.EquipmentRepository;
import com.upm.momcarerecommendation.service.EquipmentService;
import org.springframework.stereotype.Service;

@Service
public class EquipmentServiceImpl implements EquipmentService {
    private final EquipmentRepository equipmentRepository;
    public EquipmentServiceImpl(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    @Override
    public Equipment saveEquipment(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }
}
