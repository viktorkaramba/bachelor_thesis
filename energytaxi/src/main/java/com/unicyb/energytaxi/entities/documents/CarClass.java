package com.unicyb.energytaxi.entities.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarClass {
    private int ccId;
    private String name;
    private float minExperience;
    private float minNumberOfKilometers;
    private float price;
    private float minRating;
}
