package com.unicyb.energytaxi.controller.documents;

import com.unicyb.energytaxi.database.dao.documents.PricePerKilometersByTariffDAOImpl;
import com.unicyb.energytaxi.entities.documents.PricePerKilometersByTariff;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class PricePerKilometersByTariffController {
    private PricePerKilometersByTariffDAOImpl pricePerKilometersByTariffDAO;

    @GetMapping("/api/v1/documents/price-per-kilometer-by-tariff")
    public ResponseEntity getPricePerKilometersByTariff(){
        try {
            pricePerKilometersByTariffDAO = new PricePerKilometersByTariffDAOImpl();
            return ResponseEntity.ok(pricePerKilometersByTariffDAO.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get price per kilometer by tariff");
        }
    }

    @PostMapping("/api/v1/documents/price-per-kilometer-by-tariff")
    public void save(@RequestBody PricePerKilometersByTariff pricePerKilometersByTariff){
        pricePerKilometersByTariffDAO = new PricePerKilometersByTariffDAOImpl();
        pricePerKilometersByTariffDAO.add(pricePerKilometersByTariff);
    }

    @PutMapping("/api/v1/documents/price-per-kilometer-by-tariff")
    public void delete(@RequestBody PricePerKilometersByTariff pricePerKilometersByTariff){
        System.out.println("pricePerKilometersByTariff: " + pricePerKilometersByTariff);
        pricePerKilometersByTariffDAO = new PricePerKilometersByTariffDAOImpl();
        pricePerKilometersByTariffDAO.update(pricePerKilometersByTariff);
    }

    @DeleteMapping("/api/v1/documents/price-per-kilometer-by-tariff/{id}")
    public void delete(@PathVariable("id") int id){
        System.out.println("id: " + id);
        pricePerKilometersByTariffDAO = new PricePerKilometersByTariffDAOImpl();
        pricePerKilometersByTariffDAO.delete(id);
    }
}
