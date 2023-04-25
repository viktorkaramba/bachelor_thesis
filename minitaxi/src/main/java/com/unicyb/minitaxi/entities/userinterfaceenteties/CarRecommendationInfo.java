package com.unicyb.minitaxi.entities.userinterfaceenteties;

import com.unicyb.minitaxi.entities.documents.CAR_CLASSES;
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
    private CAR_CLASSES carClass;
    private float newSalary;
}
