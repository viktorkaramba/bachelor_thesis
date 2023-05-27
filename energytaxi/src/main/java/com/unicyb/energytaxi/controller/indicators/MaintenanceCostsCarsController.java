package com.unicyb.energytaxi.controller.indicators;

import com.unicyb.energytaxi.entities.documents.MaintenanceCostsCars;
import com.unicyb.energytaxi.services.documents.MaintenanceCostsCarsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;

@RestController
@CrossOrigin
public class MaintenanceCostsCarsController {

    @Autowired
    private MaintenanceCostsCarsService maintenanceCostsCarsService;

    @GetMapping("/api/v1/documents/maintenance-costs-cars")
    public ResponseEntity getMaintenanceCostsCars(){
        try {
            return ResponseEntity.ok(maintenanceCostsCarsService.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get maintenance costs cars");
        }
    }

    @PostMapping("/api/v1/documents/maintenance-costs-cars")
    public void save(@RequestBody MaintenanceCostsCars maintenanceCostsCars){
        maintenanceCostsCars.setDate(new Timestamp(new Date().getTime()));
        maintenanceCostsCarsService.add(maintenanceCostsCars);
    }

    @PutMapping("/api/v1/documents/maintenance-costs-cars")
    public void delete(@RequestBody MaintenanceCostsCars maintenanceCostsCars){
        maintenanceCostsCarsService.update(maintenanceCostsCars);
    }

    @DeleteMapping("/api/v1/documents/maintenance-costs-cars/{id}")
    public void delete(@PathVariable("id") int id){
        maintenanceCostsCarsService.delete(id);
    }
}
