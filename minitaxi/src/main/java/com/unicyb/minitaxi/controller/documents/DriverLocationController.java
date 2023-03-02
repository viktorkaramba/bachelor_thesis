package com.unicyb.minitaxi.controller.documents;

import com.unicyb.minitaxi.database.dao.documents.DriverLocationDAOImpl;
import com.unicyb.minitaxi.entities.documents.DriverLocation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class DriverLocationController {

    private DriverLocationDAOImpl driverLocationDAO;

    @GetMapping("/driver-locations")
    public ResponseEntity getDriverLocations(){
        try {
            driverLocationDAO = new DriverLocationDAOImpl();
            return ResponseEntity.ok(driverLocationDAO.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get driver locations");
        }
    }

    @PostMapping("/driver-locations")
    public void save(@RequestBody DriverLocation driverLocation){
        driverLocationDAO = new DriverLocationDAOImpl();
        driverLocationDAO.add(driverLocation);
    }

    @PutMapping("/driver-locations")
    public void update(@RequestBody DriverLocation driverLocation){
        driverLocationDAO = new DriverLocationDAOImpl();
        driverLocationDAO.update(driverLocation);
    }

    @DeleteMapping("/driver-locations/{id}")
    public void delete(@PathVariable("id") int id){
        driverLocationDAO = new DriverLocationDAOImpl();
        driverLocationDAO.delete(id);
    }
}
