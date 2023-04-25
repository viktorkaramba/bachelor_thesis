package com.unicyb.minitaxi.controller.indicators;

import com.unicyb.minitaxi.database.dao.indicators.OrderDAOImpl;
import com.unicyb.minitaxi.entities.userinterfaceenteties.Report;
import com.unicyb.minitaxi.entities.userinterfaceenteties.SendOrder;
import com.unicyb.minitaxi.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@Controller
public class OrderController {
    private OrderDAOImpl orderDAO;
    private OrderService orderService;

    @GetMapping("/api/v1/indicators/orders")
    public ResponseEntity getOrders(){
        try {
            orderDAO = new OrderDAOImpl();
            return ResponseEntity.ok(orderDAO.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get orders");
        }
    }

    @PostMapping("/api/v1/user-app/orders")
    public ResponseEntity<String> completeOrder(@RequestBody SendOrder sendOrder) {
        orderService = new OrderService();
        return orderService.completeOrder(sendOrder);
    }

    @PostMapping("/api/v1/indicators/orders-report")
    public ResponseEntity getReportOrders(@RequestBody Report report){
        try {
            orderDAO = new OrderDAOImpl();
            return ResponseEntity.ok(orderDAO.getReport(report));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get report orders");
        }
    }

    @PostMapping("/api/v1/indicators/orders-report-by-id")
    public ResponseEntity getReportOrdersById(@RequestBody Report report){
        try {
            System.out.println(report);
            orderDAO = new OrderDAOImpl();
            return ResponseEntity.ok(orderDAO.getReportById(report));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get report by id orders");
        }
    }


    @GetMapping("/api/v1/user-app/user-order-history/{id}")
    public ResponseEntity getUserOrderInfo(@PathVariable String id){
        try {
            orderDAO = new OrderDAOImpl();
            return ResponseEntity.ok(orderDAO.getUserOrderInfo(Integer.parseInt(id)));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get orders");
        }
    }

}
