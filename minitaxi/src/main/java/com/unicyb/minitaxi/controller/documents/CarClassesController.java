package com.unicyb.minitaxi.controller.documents;

import com.unicyb.minitaxi.database.dao.bonuses.MilitaryBonusesDAOImpl;
import com.unicyb.minitaxi.database.dao.documents.CarClassDAOImpl;
import com.unicyb.minitaxi.entities.bonuses.MilitaryBonuses;
import com.unicyb.minitaxi.entities.documents.CarClass;
import com.unicyb.minitaxi.entities.userinterfaceenteties.PriceByClassRequest;
import com.unicyb.minitaxi.entities.userinterfaceenteties.PriceByClassResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
public class CarClassesController {

    private CarClassDAOImpl carClassDAO;
    private MilitaryBonusesDAOImpl militaryBonusesDAO;

    @GetMapping("/api/v1/documents/car-classes")
    public ResponseEntity getCarClasses(){
        try {
            carClassDAO = new CarClassDAOImpl();
            return ResponseEntity.ok(carClassDAO.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get car classes");
        }
    }

    @PostMapping("/api/v1/documents/car-classes")
    public void save(@RequestBody CarClass carClass){
        carClassDAO = new CarClassDAOImpl();
        carClassDAO.add(carClass);
    }

    @PutMapping("/api/v1/documents/car-classes")
    public void update(@RequestBody CarClass carClass){
        System.out.println("carClass: " + carClass);
        carClassDAO = new CarClassDAOImpl();
        carClassDAO.update(carClass);
    }

    @DeleteMapping("/api/v1/documents/car-classes/{id}")
    public void delete(@PathVariable("id") int id){
        System.out.println("id: " + id);
        carClassDAO = new CarClassDAOImpl();
        carClassDAO.delete(id);
    }

    @PostMapping("/api/v1/user-app/user-order-price-by-class")
    public ResponseEntity getUserOrderPriceByClass(@RequestBody PriceByClassRequest priceByClassRequest){
        System.out.println(priceByClassRequest);
        try {
            carClassDAO = new CarClassDAOImpl();
            militaryBonusesDAO = new MilitaryBonusesDAOImpl();
            MilitaryBonuses militaryBonuses = militaryBonusesDAO.getOneByUserIdWithoutPhoto(priceByClassRequest.getUserId());
            float militarySale = 0;
            PriceByClassResponse priceByClassResponse = new PriceByClassResponse();
            System.out.println(militaryBonuses);
            if(militaryBonuses != null) {
                militarySale = militaryBonuses.getSaleValue() / 100;
                priceByClassResponse.setMilitaryBonus(true);
            }
            List<Float> floatList = new ArrayList<>();
            List<CarClass> classList = carClassDAO.getAll();
            priceByClassResponse.setClassName(classList.get(0).getName());
            for(CarClass carClass: classList){
                float price = carClass.getPrice() * priceByClassRequest.getDistance();
                float sale = price * militarySale;
                price = price - sale;
                floatList.add(price);
            }
            priceByClassResponse.setPriceByClass(floatList);
            System.out.println(priceByClassResponse);
            return ResponseEntity.ok(priceByClassResponse);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get user order price by class");
        }
    }
}
