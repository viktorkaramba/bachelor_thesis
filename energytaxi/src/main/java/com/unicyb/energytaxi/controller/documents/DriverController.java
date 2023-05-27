package com.unicyb.energytaxi.controller.documents;

import com.unicyb.energytaxi.entities.documents.Driver;
import com.unicyb.energytaxi.entities.indicators.STATUS;
import com.unicyb.energytaxi.services.documents.CarService;
import com.unicyb.energytaxi.services.documents.DriverService;
import com.unicyb.energytaxi.services.documents.FullNameService;
import com.unicyb.energytaxi.services.documents.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class DriverController {

    @Autowired
    private DriverService driverService;

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    @Autowired
    private FullNameService fullNameService;

    @GetMapping("/api/v1/documents/drivers")
    public ResponseEntity getDrivers(){
        try {
            return ResponseEntity.ok(driverService.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get drivers");
        }
    }

    @GetMapping("/api/v1/documents/drivers-resume-waiting")
    public ResponseEntity getResumeWaiting(){
        try {
            return ResponseEntity.ok(driverService.getAllByResumeStatus(STATUS.WAITING));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get drivers");
        }
    }

    @GetMapping("/api/v1/documents/driver-info/{id}")
    public ResponseEntity getDriverInfo(@PathVariable("id") int id){
        try {
            return ResponseEntity.ok(driverService.getDriverInfo(id));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get driver info");
        }
    }

    @PostMapping("/api/v1/documents/drivers")
    public ResponseEntity save(@RequestBody Driver driver){
        try {
            driverService.add(driver);
            return ResponseEntity.ok("Driver successfully added");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to add driver");
        }
    }


    @PutMapping("/api/v1/documents/drivers")
    public void update(@RequestBody Driver driver){
        driverService.update(driver);
    }

    @PutMapping("/api/v1/documents/drivers-reject-resume")
    public void rejectResume(@RequestBody Driver driver){
        userService.deleteByUserName(driver.getDriverUserName());
        driverService.delete(driver.getDriverId());
        fullNameService.delete(driver.getDriverId());
        carService.updateCarUse(driver.getCarId(), "NO");
    }

    @DeleteMapping("/api/v1/documents/drivers/{id}")
    public void delete(@PathVariable("id") int id){
        driverService.delete(id);
    }
}
