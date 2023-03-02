package com.unicyb.minitaxi.controller.indicators;

import com.unicyb.minitaxi.database.dao.documents.DriverDAOImpl;
import com.unicyb.minitaxi.database.dao.indicators.OrderDAOImpl;
import com.unicyb.minitaxi.distancematrixapi.DistanceMatrixAPi;
import com.unicyb.minitaxi.entities.documents.Driver;
import com.unicyb.minitaxi.entities.indicators.Order;
import com.unicyb.minitaxi.entities.userinterfaceenteties.Report;
import com.unicyb.minitaxi.entities.userinterfaceenteties.SendOrder;
import com.unicyb.minitaxi.services.DriverRecommendationService;
import com.unicyb.minitaxi.services.WSService;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import static com.unicyb.minitaxi.services.ExperienceService.getUpdateExperience;

@RestController
@CrossOrigin
@Controller
public class OrderController {
    private OrderDAOImpl orderDAO;
    private DriverDAOImpl driverDAO;
    private DriverRecommendationService driverRecommendationService;
    @Autowired
    private WSService wsService;

    @GetMapping("/orders")
    public ResponseEntity getOrders(){
        try {
            orderDAO = new OrderDAOImpl();
            return ResponseEntity.ok(orderDAO.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get orders");
        }
    }

    @PostMapping("/orders-report")
    public ResponseEntity getReportOrders(@RequestBody Report report){
        try {
            orderDAO = new OrderDAOImpl();
            return ResponseEntity.ok(orderDAO.getReport(report));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get report orders");
        }
    }

    @PostMapping("/orders-report-by-id")
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

    @GetMapping("/user-order-history/{id}")
    public ResponseEntity getUserOrderInfo(@PathVariable String id){
        try {
            orderDAO = new OrderDAOImpl();
            return ResponseEntity.ok(orderDAO.getUserOrderInfo(Integer.parseInt(id)));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get orders");
        }
    }

    @MessageMapping("/order-complete-message")
    public ResponseEntity getMessage(@Payload final SendOrder sendOrder) {
        try {
            System.out.println(sendOrder);
            Order order = getNewOrder(sendOrder);
            orderDAO = new OrderDAOImpl();
            orderDAO.add(order);
            driverDAO = new DriverDAOImpl();
            Driver driver = driverDAO.getOne(sendOrder.getDriverId());
            driver.setExperience(driver.getExperience() + getUpdateExperience(driver.getDate(), order.getDate()));
            driverRecommendationService = new DriverRecommendationService();
            driverRecommendationService.isApproach(driver, order.getDate());
            driverDAO.update(driver);
            return wsService.notifyOrderComplete(sendOrder);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error");
        }
    }

    public Order getNewOrder(SendOrder sendOrder) throws IOException, ParseException, InterruptedException {
        DistanceMatrixAPi distanceMatrixAPi = new DistanceMatrixAPi(sendOrder.getAddressCustomer(),
                sendOrder.getAddressDelivery());
        Float numberOfKilometers = distanceMatrixAPi.getDistance();
        Float price = sendOrder.getPrice() * numberOfKilometers;
        return new Order(sendOrder.getDriverId(), sendOrder.getAddressCustomer(), sendOrder.getAddressDelivery(),
                sendOrder.getTelephoneNumber(), price, new Timestamp(new Date().getTime()), sendOrder.getRating(),
                numberOfKilometers, sendOrder.getCustomerName(), sendOrder.getUserId());
    }
}
