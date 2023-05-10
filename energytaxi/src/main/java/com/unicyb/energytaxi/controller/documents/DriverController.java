package com.unicyb.energytaxi.controller.documents;

import com.unicyb.energytaxi.database.dao.documents.CarsDAOImpl;
import com.unicyb.energytaxi.database.dao.documents.DriverDAOImpl;
import com.unicyb.energytaxi.database.dao.documents.FullNameDAOImpl;
import com.unicyb.energytaxi.database.dao.documents.UserDAOImpl;
import com.unicyb.energytaxi.entities.documents.Driver;
import com.unicyb.energytaxi.entities.indicators.STATUS;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class DriverController {

    private DriverDAOImpl driverDAO;
    private UserDAOImpl userDAO;
    private CarsDAOImpl carsDAO;
    private FullNameDAOImpl fullNameDAO;

    @GetMapping("/api/v1/documents/drivers")
    public ResponseEntity getDrivers(){
        try {
            driverDAO = new DriverDAOImpl();
            return ResponseEntity.ok(driverDAO.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get drivers");
        }
    }

    @GetMapping("/api/v1/documents/drivers-resume-waiting")
    public ResponseEntity getResumeWaiting(){
        try {
            driverDAO = new DriverDAOImpl();
            return ResponseEntity.ok(driverDAO.getAllByResumeStatus(STATUS.WAITING));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get drivers");
        }
    }

    @GetMapping("/api/v1/documents/driver-info/{id}")
    public ResponseEntity getDriverInfo(@PathVariable("id") int id){
        try {
            driverDAO = new DriverDAOImpl();
            return ResponseEntity.ok(driverDAO.getDriverInfo(id));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get driver info");
        }
    }

    @PostMapping("/api/v1/documents/drivers")
    public ResponseEntity save(@RequestBody Driver driver){
        try {
            driverDAO = new DriverDAOImpl();
            driverDAO.add(driver);
            return ResponseEntity.ok("Driver successfully added");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to add driver");
        }
    }


    @PutMapping("/api/v1/documents/drivers")
    public void update(@RequestBody Driver driver){
        System.out.println("car: " + driver);
        driverDAO = new DriverDAOImpl();
        driverDAO.update(driver);
    }

    @PutMapping("/api/v1/documents/drivers-reject-resume")
    public void rejectResume(@RequestBody Driver driver){
        driverDAO = new DriverDAOImpl();
        userDAO = new UserDAOImpl();
        carsDAO = new CarsDAOImpl();
        fullNameDAO = new FullNameDAOImpl();
        userDAO.deleteByUserName(driver.getDriverUserName());
        driverDAO.delete(driver.getDriverId());
        fullNameDAO.delete(driver.getDriverId());
        carsDAO.updateCarUse(driver.getCarId(), "NO");
    }

    @DeleteMapping("/api/v1/documents/drivers/{id}")
    public void delete(@PathVariable("id") int id){
        System.out.println("id: " + id);
        driverDAO = new DriverDAOImpl();
        driverDAO.delete(id);
    }
}
