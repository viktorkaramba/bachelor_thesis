package com.unicyb.energytaxi.controller.indicators;

import com.unicyb.energytaxi.entities.userinterfaceenteties.Report;
import com.unicyb.energytaxi.entities.userinterfaceenteties.SendOrder;
import com.unicyb.energytaxi.services.indicators.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/api/v1/indicators/orders")
    public ResponseEntity getOrders(){
        try {
            return ResponseEntity.ok(orderService.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get orders");
        }
    }

    @PostMapping("/api/v1/user-app/orders")
    public ResponseEntity completeOrder(@RequestBody SendOrder sendOrder) {
        return orderService.completeOrder(sendOrder);
    }

    @PostMapping("/api/v1/indicators/orders-report")
    public ResponseEntity getReportOrders(@RequestBody Report report){
        try {
            return ResponseEntity.ok(orderService.getReport(report));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get report orders");
        }
    }

    @PostMapping("/api/v1/indicators/orders-report-by-id")
    public ResponseEntity getReportOrdersById(@RequestBody Report report){
        try {
            return ResponseEntity.ok(orderService.getReportById(report));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get report by id orders");
        }
    }


    @GetMapping("/api/v1/user-app/user-order-history/{id}")
    public ResponseEntity getUserOrderInfo(@PathVariable String id){
        try {
            return ResponseEntity.ok(orderService.getUserOrderInfo(Integer.parseInt(id)));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get orders");
        }
    }

}
