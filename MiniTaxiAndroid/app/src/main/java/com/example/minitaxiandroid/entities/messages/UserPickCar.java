package com.example.minitaxiandroid.entities.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPickCar {
    private int driverId;
    private String carProducer;
    private String carBrand;
    private String driverFirstName;
    private String driverSurName;
    private Float price;
}
