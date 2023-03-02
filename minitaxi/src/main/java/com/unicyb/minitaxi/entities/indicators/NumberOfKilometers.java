package com.unicyb.minitaxi.entities.indicators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NumberOfKilometers {
    private int nokId;
    private Timestamp date;
    private float numbers;
    private int driverId;
}
