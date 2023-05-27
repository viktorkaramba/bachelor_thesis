package com.unicyb.energytaxi.controller.indicators;

import com.unicyb.energytaxi.services.indicators.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/indicators/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping()
    public ResponseEntity getAddresses(){
        try {
            return ResponseEntity.ok(addressService.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get addresses");
        }
    }
}
