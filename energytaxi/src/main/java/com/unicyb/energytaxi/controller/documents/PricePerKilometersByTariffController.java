package com.unicyb.energytaxi.controller.documents;

import com.unicyb.energytaxi.entities.documents.PricePerKilometersByTariff;
import com.unicyb.energytaxi.services.documents.PricePerKilometersByTariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class PricePerKilometersByTariffController {

    @Autowired
    private PricePerKilometersByTariffService pricePerKilometersByTariffService;

    @GetMapping("/api/v1/documents/price-per-kilometer-by-tariff")
    public ResponseEntity getPricePerKilometersByTariff(){
        try {
            return ResponseEntity.ok(pricePerKilometersByTariffService.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get price per kilometer by tariff");
        }
    }

    @PostMapping("/api/v1/documents/price-per-kilometer-by-tariff")
    public void save(@RequestBody PricePerKilometersByTariff pricePerKilometersByTariff){
        pricePerKilometersByTariffService.add(pricePerKilometersByTariff);
    }

    @PutMapping("/api/v1/documents/price-per-kilometer-by-tariff")
    public void delete(@RequestBody PricePerKilometersByTariff pricePerKilometersByTariff){
        System.out.println("pricePerKilometersByTariff: " + pricePerKilometersByTariff);
        pricePerKilometersByTariffService.update(pricePerKilometersByTariff);
    }

    @DeleteMapping("/api/v1/documents/price-per-kilometer-by-tariff/{id}")
    public void delete(@PathVariable("id") int id){
        System.out.println("id: " + id);
        pricePerKilometersByTariffService.delete(id);
    }
}
