package com.unicyb.energytaxi.entities.userinterfaceenteties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo {
    private Timestamp date;
    private String addressCustomer;
    private String addressDelivery;
    private String telephoneCustomer;
    private float price;
    private float rating;
    private float numberOfKilometers;
    private String customerName;
}
