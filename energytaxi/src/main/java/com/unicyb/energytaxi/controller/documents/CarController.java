package com.unicyb.energytaxi.controller.documents;

import com.unicyb.energytaxi.entities.auth.CarUpdateRequest;
import com.unicyb.energytaxi.entities.documents.Car;
import com.unicyb.energytaxi.entities.userinterfaceenteties.ResponseMessage;
import com.unicyb.energytaxi.services.documents.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;

@RestController
@CrossOrigin
public class CarController {

    @Autowired
    private CarService carService;
    @GetMapping("/api/v1/documents/cars")
    public ResponseEntity getCars(){
        try {
            return ResponseEntity.ok(carService.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get cars");
        }
    }


    @PostMapping("/api/v1/documents/cars")
    public void save(@RequestBody Car car){
        car.setDate(new Timestamp(new Date().getTime()));
        carService.add(car);
    }

    @PostMapping("/api/v1/documents/car-status")
    public void updateCarStatus(@RequestBody ResponseMessage status){
        carService.updateCarUse(Integer.parseInt(status.getUserId()), status.getContent());
    }


    @PutMapping("/api/v1/documents/cars")
    public void update(@RequestBody Car car){
        carService.update(car);
    }


    @PutMapping("/api/v1/documents/cars-resume-answer")
    public void updateResumeAnswer(@RequestBody CarUpdateRequest carUpdateRequest){
        carService.updateCarUse(carUpdateRequest.getNewCarId(), "YES");
        carService.updateCarUse(carUpdateRequest.getOldCarId(), "NO");
    }

    @DeleteMapping("/api/v1/documents/cars/{id}")
    public void delete(@PathVariable("id") int id){
        carService.delete(id);
    }
}
