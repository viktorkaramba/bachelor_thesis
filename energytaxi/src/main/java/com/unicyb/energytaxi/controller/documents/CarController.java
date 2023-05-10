package com.unicyb.energytaxi.controller.documents;

import com.unicyb.energytaxi.database.dao.documents.CarsDAOImpl;
import com.unicyb.energytaxi.entities.auth.CarUpdateRequest;
import com.unicyb.energytaxi.entities.documents.Car;
import com.unicyb.energytaxi.entities.userinterfaceenteties.ResponseMessage;
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
            System.out.println(carsDAO.getAll());
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

    @PostMapping("/api/v1/documents/car-status")
    public void updateCarStatus(@RequestBody ResponseMessage status){
        carsDAO = new CarsDAOImpl();
        carsDAO.updateCarUse(Integer.parseInt(status.getUserId()), status.getContent());
    }


    @PutMapping("/api/v1/documents/cars")
    public void update(@RequestBody Car car){
        System.out.println("car: " + car);
        carsDAO = new CarsDAOImpl();
        carsDAO.update(car);
    }


    @PutMapping("/api/v1/documents/cars-resume-answer")
    public void updateResumeAnswer(@RequestBody CarUpdateRequest carUpdateRequest){
        carsDAO = new CarsDAOImpl();
        carsDAO.updateCarUse(carUpdateRequest.getNewCarId(), "YES");
        carsDAO.updateCarUse(carUpdateRequest.getOldCarId(), "NO");
    }

    @DeleteMapping("/api/v1/documents/cars/{id}")
    public void delete(@PathVariable("id") int id){
        System.out.println("id: " + id);
        carsDAO = new CarsDAOImpl();
        carsDAO.delete(id);
    }
}
