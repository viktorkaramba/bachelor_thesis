package com.unicyb.minitaxi.controller.documents;

import com.unicyb.minitaxi.database.dao.documents.CarsDAOImpl;
import com.unicyb.minitaxi.entities.documents.Car;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;

@RestController
@CrossOrigin
public class CarController {

    private CarsDAOImpl carsDAO;
    @GetMapping("/api/v1/documents/cars")
    public ResponseEntity getCars(){
        try {
            carsDAO = new CarsDAOImpl();
            return ResponseEntity.ok(carsDAO.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get cars");
        }
    }

    @PostMapping("/api/v1/documents/cars")
    public void save(@RequestBody Car car){
        carsDAO = new CarsDAOImpl();
        car.setDate(new Timestamp(new Date().getTime()));
        System.out.println(car);
        carsDAO.add(car);
    }

    @PutMapping("/api/v1/documents/cars")
    public void update(@RequestBody Car car){
        System.out.println("car: " + car);
        carsDAO = new CarsDAOImpl();
        carsDAO.update(car);
    }

    @DeleteMapping("/api/v1/documents/cars/{id}")
    public void delete(@PathVariable("id") int id){
        System.out.println("id: " + id);
        carsDAO = new CarsDAOImpl();
        carsDAO.delete(id);
    }
}
