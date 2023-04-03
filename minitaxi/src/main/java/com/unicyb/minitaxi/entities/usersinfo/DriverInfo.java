package com.unicyb.minitaxi.entities.usersinfo;

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

    public DriverInfo(int driverId, String driverFirstName, String driverSurName, String carProducer,
                      String carBrand, String inOrder){
        this.driverId = driverId;
        this.driverFirstName = driverFirstName;
        this.driverSurName = driverSurName;
        this.carProducer = carProducer;
        this.carBrand = carBrand;
        this.inOrder = inOrder;
    }
}
