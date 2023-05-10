package com.unicyb.energytaxi.entities.usersinfo;

import com.unicyb.energytaxi.entities.documents.CAR_CLASSES;
import com.unicyb.energytaxi.entities.documents.DRIVER_STATUS;
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
