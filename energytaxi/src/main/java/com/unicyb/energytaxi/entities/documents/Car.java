package com.unicyb.energytaxi.entities.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Car {
    private int carId;
    private Timestamp date;
    private String brand;
    private String producer;
    private int numberOfSeats;
    private int ccID;
    private String inUse;
    private String inOrder;
}
