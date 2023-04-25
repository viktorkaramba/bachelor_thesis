package com.unicyb.minitaxi.controller.documents;

import com.unicyb.minitaxi.database.dao.documents.DriverDAOImpl;
import com.unicyb.minitaxi.entities.documents.Driver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class DriverController {

    private DriverDAOImpl driverDAO;

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
    public void delete(@RequestBody Driver driver){
        System.out.println("car: " + driver);
        driverDAO = new DriverDAOImpl();
        driverDAO.update(driver);
    }


    @DeleteMapping("/api/v1/documents/drivers/{id}")
    public void delete(@PathVariable("id") int id){
        System.out.println("id: " + id);
        driverDAO = new DriverDAOImpl();
        driverDAO.delete(id);
    }
}
