package com.unicyb.minitaxi.controller.documents;

import com.unicyb.minitaxi.database.dao.documents.CarClassDAOImpl;
import com.unicyb.minitaxi.distancematrixapi.DistanceMatrixAPi;
import com.unicyb.minitaxi.entities.documents.CarClass;
import com.unicyb.minitaxi.entities.userinterfaceenteties.PriceByClass;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/user-order-price-by-class/{addressFrom}/{addressTo}")
    public ResponseEntity getUserOrderPriceByClass(@PathVariable("addressFrom") String addressFrom,
                                                   @PathVariable("addressTo") String addressTo){
        try {
            carClassDAO = new CarClassDAOImpl();
            List<Float> floatList =new ArrayList<>();
            DistanceMatrixAPi distanceMatrixAPi = new DistanceMatrixAPi(addressFrom, addressTo);
            float numberOfKilometers = distanceMatrixAPi.getDistance();
            List<CarClass> classList = carClassDAO.getAll();
            PriceByClass priceByClassList = new PriceByClass();
            priceByClassList.setDistance(numberOfKilometers);
            priceByClassList.setClassName(classList.get(0).getName());
            for(CarClass carClass: classList){
                floatList.add(carClass.getPrice() * numberOfKilometers);
            }
            priceByClassList.setPriceByClass(floatList);
            return ResponseEntity.ok(priceByClassList);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get user order price by class");
        }
    }
}
