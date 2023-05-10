package com.unicyb.energytaxi.controller.userinterface;

import com.unicyb.energytaxi.database.dao.userinterface.DriverProfileInfoDAOImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class DriverProfileInfo {
    private DriverProfileInfoDAOImpl driverProfileInfoDAO;

    @GetMapping("/api/v1/driver-app/driver-profile/{id}")
    public ResponseEntity getDriverProfile(@PathVariable String id){
        try {
            System.out.println(id);
            driverProfileInfoDAO = new DriverProfileInfoDAOImpl();
            return ResponseEntity.ok(driverProfileInfoDAO.getByDriverId(Integer.parseInt(id)));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get driver profile");
        }
    }
}
