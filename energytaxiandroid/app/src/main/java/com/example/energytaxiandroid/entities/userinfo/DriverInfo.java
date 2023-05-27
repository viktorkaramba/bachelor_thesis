package com.example.energytaxiandroid.entities.userinfo;

import com.example.energytaxiandroid.entities.document.CAR_CLASSES;
import com.example.energytaxiandroid.entities.document.DRIVER_STATUS;
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
    private CAR_CLASSES carClass;
    private double latitude;
    private double longitude;
    private DRIVER_STATUS status;
}
