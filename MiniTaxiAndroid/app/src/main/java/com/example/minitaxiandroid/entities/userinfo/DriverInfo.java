package com.example.minitaxiandroid.entities.userinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverInfo {
    private int driverId;
    private String driverFirstName;
    private String driverSurName;
    private String carProducer;
    private String carBrand;
    private String inOrder;
    private String latitude = "";
    private String longitude = "";
}
