package com.example.minitaxiandroid.entities.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendOrder {
    private int driverId;
    private int userId;
    private String customerName;
    private String addressCustomer;
    private String addressDelivery;
    private String telephoneNumber;
    private Float price;
    private Float rating;
}
