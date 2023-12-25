package com.upm.momcarerecommendation.controller;

import com.upm.momcarerecommendation.domain.entity.Equipment;
import com.upm.momcarerecommendation.service.EquipmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;
    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @PostMapping
    public ResponseEntity<Equipment> addEquipment(@RequestBody Equipment equipment) {
        Equipment savedEquipment = equipmentService.saveEquipment(equipment);
        return ResponseEntity.ok(savedEquipment);
    }
}
