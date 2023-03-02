package com.unicyb.minitaxi.entities.userinterfaceenteties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSendDate {
    private int driverId;
    private int userId;
    private String customerName;
    private String addressCustomer;
    private String addressDelivery;
    private String telephoneNumber;
}