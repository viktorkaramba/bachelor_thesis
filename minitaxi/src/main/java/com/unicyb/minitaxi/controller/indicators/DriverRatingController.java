package com.unicyb.minitaxi.controller.indicators;

import com.unicyb.minitaxi.database.dao.indicators.DriverRatingDAOImpl;
import com.unicyb.minitaxi.entities.userinterfaceenteties.Report;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class DriverRatingController {
    private DriverRatingDAOImpl driverRatingDAO;

    @GetMapping("/drivers-ratings")
    public ResponseEntity getDriversRatings(){
        try {
            driverRatingDAO = new DriverRatingDAOImpl();
            return ResponseEntity.ok(driverRatingDAO.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get drivers ratings");
        }
    }

    @PostMapping("/drivers-ratings-report")
    public ResponseEntity getReportDriversRatings(@RequestBody Report report){
        System.out.println(report);
        try {
            driverRatingDAO = new DriverRatingDAOImpl();
            return ResponseEntity.ok(driverRatingDAO.getReport(report));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get report drivers ratings");
        }
    }

    @PostMapping("/drivers-ratings-report-by-id")
    public ResponseEntity getReportDriversRatingsById(@RequestBody Report report){
        try {
            driverRatingDAO = new DriverRatingDAOImpl();
            return ResponseEntity.ok(driverRatingDAO.getReportById(report));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get report by id drivers ratings");
        }
    }
}