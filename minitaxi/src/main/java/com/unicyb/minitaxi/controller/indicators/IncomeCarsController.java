package com.unicyb.minitaxi.controller.indicators;

import com.unicyb.minitaxi.database.dao.indicators.IncomeCarsDAOImpl;
import com.unicyb.minitaxi.entities.userinterfaceenteties.Report;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class IncomeCarsController {
    private IncomeCarsDAOImpl incomeCarsDAO;

    @GetMapping("/income-cars")
    public ResponseEntity getIncomeCars(){
        try {
            incomeCarsDAO = new IncomeCarsDAOImpl();
            return ResponseEntity.ok(incomeCarsDAO.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get income cars");
        }
    }

    @PostMapping("/income-cars-report")
    public ResponseEntity getReportIncomeCars(@RequestBody Report report){
        try {
            incomeCarsDAO = new IncomeCarsDAOImpl();
            return ResponseEntity.ok(incomeCarsDAO.getReport(report));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get report income cars");
        }
    }

    @PostMapping("/income-cars-report-by-id")
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
