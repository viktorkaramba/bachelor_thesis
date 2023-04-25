package com.unicyb.minitaxi.controller.indicators;

import com.unicyb.minitaxi.database.dao.indicators.WorkloadDriversDAOImpl;
import com.unicyb.minitaxi.entities.userinterfaceenteties.Report;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class WorkloadDriversController {
    private WorkloadDriversDAOImpl workloadDriversDAO;

    @GetMapping("/api/v1/indicators/workloads-drivers")
    public ResponseEntity getWorkloadDrivers(){
        try {
            workloadDriversDAO = new WorkloadDriversDAOImpl();
            return ResponseEntity.ok(workloadDriversDAO.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get workloads drivers");
        }
    }

    @PostMapping("/api/v1/indicators/workloads-drivers-report")
    public ResponseEntity getWorkloadDriversReport(@RequestBody Report report){
        try {
            workloadDriversDAO = new WorkloadDriversDAOImpl();
            return ResponseEntity.ok(workloadDriversDAO.getReport(report));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get report workloads drivers");
        }
    }

    @PostMapping("/api/v1/indicators/workloads-drivers-report-by-id")
    public ResponseEntity getWorkloadDriversReportById(@RequestBody Report report){
        try {
            workloadDriversDAO = new WorkloadDriversDAOImpl();
            return ResponseEntity.ok(workloadDriversDAO.getReportById(report));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get report by id workloads drivers");
        }
    }
}
