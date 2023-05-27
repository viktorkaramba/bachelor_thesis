package com.example.energytaxiandroid.entities.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverProfileInfo {
    private int driverId;
    private String driverUserName;
    private String driverFirstName;
    private String driverSurName;
    private String driverPatronymic;
    private String carProducer;
    private String carBrand;
    private float experience;
    private float salary;
}
