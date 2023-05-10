package com.unicyb.energytaxi.controller.indicators;

import com.unicyb.energytaxi.database.dao.indicators.NumberOfKilometersDAOImpl;
import com.unicyb.energytaxi.entities.userinterfaceenteties.Report;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class NumberOfKilometersController {
    private NumberOfKilometersDAOImpl numberOfKilometersDAO;

    @GetMapping("/api/v1/indicators/number-of-kilometers")
    public ResponseEntity getNumberOfKilometersDAOImpl(){
        try {
            numberOfKilometersDAO = new NumberOfKilometersDAOImpl();
            return ResponseEntity.ok(numberOfKilometersDAO.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get number of kilometers");
        }
    }

    @PostMapping("/api/v1/indicators/number-of-kilometers-report")
    public ResponseEntity getNumberOfKilometersReport(@RequestBody Report report){
        try {
            numberOfKilometersDAO = new NumberOfKilometersDAOImpl();
            return ResponseEntity.ok(numberOfKilometersDAO.getReport(report));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get report number of kilometers");
        }
    }

    @PostMapping("/api/v1/indicators/number-of-kilometers-report-by-id")
    public ResponseEntity getNumberOfKilometersReportById(@RequestBody Report report){
        try {
            numberOfKilometersDAO = new NumberOfKilometersDAOImpl();
            return ResponseEntity.ok(numberOfKilometersDAO.getReportById(report));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get report by id number of kilometers");
        }
    }
}
