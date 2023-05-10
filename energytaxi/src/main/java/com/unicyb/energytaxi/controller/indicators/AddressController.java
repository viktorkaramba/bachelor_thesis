package com.unicyb.energytaxi.controller.indicators;

import com.unicyb.energytaxi.database.dao.indicators.AddressDAOImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/indicators/addresses")
public class AddressController {
    private AddressDAOImpl addressDAO;

    @GetMapping()
    public ResponseEntity getAddresses(){
        try {
            addressDAO = new AddressDAOImpl();
            return ResponseEntity.ok(addressDAO.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get addresses");
        }
    }
}
