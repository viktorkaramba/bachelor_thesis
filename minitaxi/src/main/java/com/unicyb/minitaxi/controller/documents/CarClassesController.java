package com.unicyb.minitaxi.controller.documents;

import com.unicyb.minitaxi.database.dao.documents.CarClassDAOImpl;
import com.unicyb.minitaxi.entities.documents.CarClass;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class CarClassesController {

    private CarClassDAOImpl carClassDAO;

    @GetMapping("/car-classes")
    public ResponseEntity getCarClasses(){
        try {
            carClassDAO = new CarClassDAOImpl();
            return ResponseEntity.ok(carClassDAO.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get car classes");
        }
    }

    @PostMapping("/car-classes")
    public void save(@RequestBody CarClass carClass){
        carClassDAO = new CarClassDAOImpl();
        carClassDAO.add(carClass);
    }

    @PutMapping("/car-classes")
    public void update(@RequestBody CarClass carClass){
        System.out.println("carClass: " + carClass);
        carClassDAO = new CarClassDAOImpl();
        carClassDAO.update(carClass);
    }

    @DeleteMapping("/car-classes/{id}")
    public void delete(@PathVariable("id") int id){
        System.out.println("id: " + id);
        carClassDAO = new CarClassDAOImpl();
        carClassDAO.delete(id);
    }
}
