package com.example.minitaxiandroid.entities.messages;

import com.example.minitaxiandroid.entities.document.CAR_CLASSES;
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
    private String userComment;
    private Float price;
    private Boolean isUseSale;
    private CAR_CLASSES carClass;
    private Float distance;
    private Float rating;
    private int rankId;
}
