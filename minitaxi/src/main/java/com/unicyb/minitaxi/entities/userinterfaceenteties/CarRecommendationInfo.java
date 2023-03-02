package com.unicyb.minitaxi.entities.userinterfaceenteties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarRecommendationInfo {
    private int driverCarRecId;
    private int driverId;
    private int carId;
    private String carProducer;
    private String carBrand;
    private float pricePerKilometer;
    private String carClass;
    private float newSalary;
}
