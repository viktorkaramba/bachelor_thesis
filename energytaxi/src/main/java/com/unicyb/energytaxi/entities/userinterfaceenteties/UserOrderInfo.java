package com.unicyb.energytaxi.entities.userinterfaceenteties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOrderInfo {
    private String driverName;
    private String carName;
    private String addressCustomer;
    private String addressDelivery;
    private Float rating;
    private Float price;
    private Timestamp date;
}
