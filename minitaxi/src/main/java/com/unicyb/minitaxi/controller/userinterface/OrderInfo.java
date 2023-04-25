package com.unicyb.minitaxi.controller.userinterface;

import com.unicyb.minitaxi.database.dao.userinterface.OrderInfoDAOImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class OrderInfo {
    private OrderInfoDAOImpl orderInfoDAO;

    @GetMapping("/api/v1/driver-app/order-info-profile/{id}")
    public ResponseEntity getOrderInfo(@PathVariable String id){
        try {
            orderInfoDAO = new OrderInfoDAOImpl();
            return ResponseEntity.ok(orderInfoDAO.getByDriverId(Integer.parseInt(id)));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get order info");
        }
    }
}
