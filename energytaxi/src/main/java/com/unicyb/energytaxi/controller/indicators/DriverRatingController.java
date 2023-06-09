package com.unicyb.energytaxi.controller.indicators;

import com.unicyb.energytaxi.entities.userinterfaceenteties.Report;
import com.unicyb.energytaxi.services.indicators.DriverRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class DriverRatingController {

    @Autowired
    private DriverRatingService driverRatingService;

    @GetMapping("/api/v1/indicators/drivers-ratings")
    public ResponseEntity getDriversRatings(){
        try {
            return ResponseEntity.ok(driverRatingService.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get drivers ratings");
        }
    }

    @PostMapping("/api/v1/indicators/drivers-ratings-report")
    public ResponseEntity getReportDriversRatings(@RequestBody Report report){
        try {
            return ResponseEntity.ok(driverRatingService.getReport(report));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get report drivers ratings");
        }
    }

    @PostMapping("/api/v1/indicators/drivers-ratings-report-by-id")
    public ResponseEntity getReportDriversRatingsById(@RequestBody Report report){
        try {
            return ResponseEntity.ok(driverRatingService.getReportById(report));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get report by id drivers ratings");
        }
    }
}
