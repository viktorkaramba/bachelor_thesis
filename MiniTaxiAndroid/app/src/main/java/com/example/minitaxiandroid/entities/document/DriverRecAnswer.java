package com.example.minitaxiandroid.entities.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverRecAnswer {
    private int dcrId;
    private float newSalary;
    private int carId;
    private int driverId;
    private String answer;
}
