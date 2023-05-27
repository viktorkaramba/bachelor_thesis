package com.unicyb.energytaxi.controller.userinterface;

import com.unicyb.energytaxi.services.userinterface.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class OrderInfo {

    @Autowired
    private OrderInfoService orderInfoService;

    @GetMapping("/api/v1/driver-app/order-info-profile/{id}")
    public ResponseEntity getOrderInfo(@PathVariable String id){
        try {
            return ResponseEntity.ok(orderInfoService.getByDriverId(Integer.parseInt(id)));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get order info");
        }
    }
}
