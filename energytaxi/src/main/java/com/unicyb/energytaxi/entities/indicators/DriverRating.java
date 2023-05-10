package com.unicyb.energytaxi.entities.indicators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverRating {
    private int driversRatingsId;
    private int driverId;
    private Timestamp date;
    private float rating;
}
