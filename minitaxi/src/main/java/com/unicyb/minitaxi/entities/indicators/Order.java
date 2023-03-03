package com.unicyb.minitaxi.entities.indicators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private int orderId;
    private int driverId;
    private String addressCustomer;
    private String addressDelivery;
    private String telephoneCustomer;
    private float price;
    private Timestamp date;
    private float rating;
    private float numberOfKilometers;
    private String customerName;
    private int userId;
    private String userComment;

    public Order(int driverId, String addressCustomer, String addressDelivery, String telephoneCustomer, float price,
                 Timestamp date, float rating, float numberOfKilometers, String customerName, int userId, String userComment) {
        this.driverId = driverId;
        this.addressCustomer = addressCustomer;
        this.addressDelivery = addressDelivery;
        this.telephoneCustomer = telephoneCustomer;
        this.price = price;
        this.date = date;
        this.rating = rating;
        this.numberOfKilometers = numberOfKilometers;
        this.customerName = customerName;
        this.userId = userId;
        this.userComment = userComment;
    }
}
