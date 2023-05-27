package com.unicyb.energytaxi.controller.documents;

import com.unicyb.energytaxi.entities.bonuses.MilitaryBonuses;
import com.unicyb.energytaxi.entities.documents.CAR_CLASSES;
import com.unicyb.energytaxi.entities.documents.CarClass;
import com.unicyb.energytaxi.entities.documents.PeriodsOfDay;
import com.unicyb.energytaxi.entities.documents.PricePerKilometersByTariff;
import com.unicyb.energytaxi.entities.userinterfaceenteties.PriceByClassRequest;
import com.unicyb.energytaxi.entities.userinterfaceenteties.PriceByClassResponse;
import com.unicyb.energytaxi.services.other.DateService;
import com.unicyb.energytaxi.services.bonuses.MilitaryBonusesService;
import com.unicyb.energytaxi.services.documents.CarClassService;
import com.unicyb.energytaxi.services.documents.PricePerKilometersByTariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
public class CarClassesController {

    @Autowired
    private CarClassService carClassService;

    @Autowired
    private MilitaryBonusesService militaryBonusesService;

    @Autowired
    private PricePerKilometersByTariffService pricePerKilometersByTariffService;

    @GetMapping("/api/v1/documents/car-classes")
    public ResponseEntity getCarClasses(){
        try {
            return ResponseEntity.ok(carClassService.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get car classes");
        }
    }

    @PostMapping("/api/v1/documents/car-classes")
    public void save(@RequestBody CarClass carClass){
        carClassService.add(carClass);
    }

    @PutMapping("/api/v1/documents/car-classes")
    public void update(@RequestBody CarClass carClass){
        System.out.println("carClass: " + carClass);
        carClassService.update(carClass);
    }

    @DeleteMapping("/api/v1/documents/car-classes/{id}")
    public void delete(@PathVariable("id") int id){
        System.out.println("id: " + id);
        carClassService.delete(id);
    }

    @PostMapping("/api/v1/user-app/user-order-price-by-class")
    public ResponseEntity getUserOrderPriceByClass(@RequestBody PriceByClassRequest priceByClassRequest){
        System.out.println(priceByClassRequest);
        try {
            MilitaryBonuses militaryBonuses = militaryBonusesService.getOneByUserIdWithoutPhoto(priceByClassRequest.getUserId());
            float militarySale = 0;
            PriceByClassResponse priceByClassResponse = new PriceByClassResponse();
            System.out.println(militaryBonuses);
            if(militaryBonuses != null) {
                militarySale = militaryBonuses.getSaleValue() / 100;
                priceByClassResponse.setMilitaryBonus(true);
            }
            List<Float> floatList = new ArrayList<>();
            List<PricePerKilometersByTariff> pricePerKilometersByTariffList = pricePerKilometersByTariffService.getAll();
            PeriodsOfDay period = getPeriod(pricePerKilometersByTariffList);
            priceByClassResponse.setClassName(CAR_CLASSES.NO.name());
            for(PricePerKilometersByTariff pricePerKilometersByTariff: pricePerKilometersByTariffList){
                float price = 0;
                if(period.equals(PeriodsOfDay.MORNING)){
                    price = pricePerKilometersByTariff.getMorning() * priceByClassRequest.getDistance();
                }
                if(period.equals(PeriodsOfDay.DAY)){
                    price = pricePerKilometersByTariff.getDay() * priceByClassRequest.getDistance();
                }
                if(period.equals(PeriodsOfDay.EVENING)){
                    price = pricePerKilometersByTariff.getEvening() * priceByClassRequest.getDistance();
                }
                if(period.equals(PeriodsOfDay.NIGHT)){
                    price = pricePerKilometersByTariff.getNight() * priceByClassRequest.getDistance();
                }
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

    private PeriodsOfDay getPeriod(List<PricePerKilometersByTariff> pricePerKilometersByTariffList) {
        if(DateService.recognize(java.time.LocalTime.now()).equals(PeriodsOfDay.MORNING.name())){
            return PeriodsOfDay.MORNING;
        }
        if(DateService.recognize(java.time.LocalTime.now()).equals(PeriodsOfDay.DAY.name())){
            return PeriodsOfDay.DAY;
        }
        if(DateService.recognize(java.time.LocalTime.now()).equals(PeriodsOfDay.EVENING.name())){
            return PeriodsOfDay.EVENING;
        }
        else {
            return PeriodsOfDay.NIGHT;
        }
    }
}
