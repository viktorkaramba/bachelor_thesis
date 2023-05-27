package com.unicyb.energytaxi.controller.indicators;

import com.unicyb.energytaxi.entities.userinterfaceenteties.Report;
import com.unicyb.energytaxi.services.indicators.NumberOfKilometersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class NumberOfKilometersController {

    @Autowired
    private NumberOfKilometersService numberOfKilometersService;

    @GetMapping("/api/v1/indicators/number-of-kilometers")
    public ResponseEntity getNumberOfKilometersDAOImpl(){
        try {
            return ResponseEntity.ok(numberOfKilometersService.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get number of kilometers");
        }
    }

    @PostMapping("/api/v1/indicators/number-of-kilometers-report")
    public ResponseEntity getNumberOfKilometersReport(@RequestBody Report report){
        try {
            return ResponseEntity.ok(numberOfKilometersService.getReport(report));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get report number of kilometers");
        }
    }

    @PostMapping("/api/v1/indicators/number-of-kilometers-report-by-id")
    public ResponseEntity getNumberOfKilometersReportById(@RequestBody Report report){
        try {
            return ResponseEntity.ok(numberOfKilometersService.getReportById(report));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get report by id number of kilometers");
        }
    }
}
