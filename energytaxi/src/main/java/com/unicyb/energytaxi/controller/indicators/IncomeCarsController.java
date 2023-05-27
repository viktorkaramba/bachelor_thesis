package com.unicyb.energytaxi.controller.indicators;

import com.unicyb.energytaxi.entities.userinterfaceenteties.Report;
import com.unicyb.energytaxi.services.indicators.IncomeCarsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class IncomeCarsController {

    @Autowired
    private IncomeCarsService incomeCarsService;

    @GetMapping("/api/v1/indicators/income-cars")
    public ResponseEntity getIncomeCars(){
        try {
            return ResponseEntity.ok(incomeCarsService.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get income cars");
        }
    }

    @PostMapping("/api/v1/indicators/income-cars-report")
    public ResponseEntity getReportIncomeCars(@RequestBody Report report){
        try {
            return ResponseEntity.ok(incomeCarsService.getReport(report));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get report income cars");
        }
    }

    @PostMapping("/api/v1/indicators/income-cars-report-by-id")
    public ResponseEntity getReportByIdIncomeCars(@RequestBody Report report){
        try {
            return ResponseEntity.ok(incomeCarsService.getReportById(report));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get report by id income cars");
        }
    }
}
