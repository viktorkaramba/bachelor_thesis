package com.unicyb.energytaxi.controller.documents;

import com.unicyb.energytaxi.database.dao.documents.MaintenanceCostsCarsDAOImpl;
import com.unicyb.energytaxi.entities.documents.MaintenanceCostsCars;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;

@RestController
@CrossOrigin
public class MaintenanceCostsCarsController {
    private MaintenanceCostsCarsDAOImpl maintenanceCostsCarsDAO;

    @GetMapping("/api/v1/documents/maintenance-costs-cars")
    public ResponseEntity getMaintenanceCostsCars(){
        try {
            maintenanceCostsCarsDAO = new MaintenanceCostsCarsDAOImpl();
            return ResponseEntity.ok(maintenanceCostsCarsDAO.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get maintenance costs cars");
        }
    }

    @PostMapping("/api/v1/documents/maintenance-costs-cars")
    public void save(@RequestBody MaintenanceCostsCars maintenanceCostsCars){
        maintenanceCostsCarsDAO = new MaintenanceCostsCarsDAOImpl();
        maintenanceCostsCars.setDate(new Timestamp(new Date().getTime()));
        maintenanceCostsCarsDAO.add(maintenanceCostsCars);
    }

    @PutMapping("/api/v1/documents/maintenance-costs-cars")
    public void delete(@RequestBody MaintenanceCostsCars maintenanceCostsCars){
        System.out.println("maintenance-costs-cars: " + maintenanceCostsCars);
        maintenanceCostsCarsDAO = new MaintenanceCostsCarsDAOImpl();
        maintenanceCostsCarsDAO.update(maintenanceCostsCars);
    }

    @DeleteMapping("/api/v1/documents/maintenance-costs-cars/{id}")
    public void delete(@PathVariable("id") int id){
        System.out.println("id: " + id);
        maintenanceCostsCarsDAO = new MaintenanceCostsCarsDAOImpl();
        maintenanceCostsCarsDAO.delete(id);
    }
}