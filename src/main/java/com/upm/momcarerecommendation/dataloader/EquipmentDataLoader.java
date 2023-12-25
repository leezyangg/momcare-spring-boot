package com.upm.momcarerecommendation.dataloader;

import com.upm.momcarerecommendation.domain.entity.Equipment;
import com.upm.momcarerecommendation.domain.repository.EquipmentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class EquipmentDataLoader implements CommandLineRunner {
    private final EquipmentRepository equipmentRepository;
    public EquipmentDataLoader(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    @Override
    public void run(String... args){
        addEquipmentIfNotFound("Yoga Mat");
        addEquipmentIfNotFound("Dumbbells");
        addEquipmentIfNotFound("Stationary Bike");
        addEquipmentIfNotFound("Treadmill");
        addEquipmentIfNotFound("Elliptical Trainer");
        addEquipmentIfNotFound("Kettlebells");
        addEquipmentIfNotFound("Rowing Machine");
        addEquipmentIfNotFound("Exercise Ball");
        addEquipmentIfNotFound("Resistance Bands");
        addEquipmentIfNotFound("Jump Rope");
        addEquipmentIfNotFound("Pull-up Bar");
        addEquipmentIfNotFound("Medicine Ball");
        addEquipmentIfNotFound("Pilates Reformer");
    }



    private void addEquipmentIfNotFound(String name) {
        if (!equipmentRepository.existsByName(name)) {
            equipmentRepository.save(new Equipment(name));
        }
    }
}
