package com.unicyb.energytaxi.controller.userinterface;

import com.unicyb.energytaxi.services.userinterface.DriverProfileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class DriverProfileInfo {

    @Autowired
    private DriverProfileInfoService driverProfileInfoService;

    @GetMapping("/api/v1/driver-app/driver-profile/{id}")
    public ResponseEntity getDriverProfile(@PathVariable String id){
        try {
            return ResponseEntity.ok(driverProfileInfoService.getByDriverId(Integer.parseInt(id)));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get driver profile");
        }
    }
}
