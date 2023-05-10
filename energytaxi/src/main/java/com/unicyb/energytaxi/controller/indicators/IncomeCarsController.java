package com.unicyb.energytaxi.controller.indicators;

import com.unicyb.energytaxi.database.dao.indicators.IncomeCarsDAOImpl;
import com.unicyb.energytaxi.entities.userinterfaceenteties.Report;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class IncomeCarsController {
    private IncomeCarsDAOImpl incomeCarsDAO;

    @GetMapping("/api/v1/indicators/income-cars")
    public ResponseEntity getIncomeCars(){
        try {
            incomeCarsDAO = new IncomeCarsDAOImpl();
            return ResponseEntity.ok(incomeCarsDAO.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get income cars");
        }
    }

    @PostMapping("/api/v1/indicators/income-cars-report")
    public ResponseEntity getReportIncomeCars(@RequestBody Report report){
        try {
            incomeCarsDAO = new IncomeCarsDAOImpl();
            return ResponseEntity.ok(incomeCarsDAO.getReport(report));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get report income cars");
        }
    }

    @PostMapping("/api/v1/indicators/income-cars-report-by-id")
    public ResponseEntity getReportByIdIncomeCars(@RequestBody Report report){
        try {
            incomeCarsDAO = new IncomeCarsDAOImpl();
            return ResponseEntity.ok(incomeCarsDAO.getReportById(report));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get report by id income cars");
        }
    }
}
