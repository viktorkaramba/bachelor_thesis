package com.unicyb.minitaxi.entities.indicators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncomeCars {
    private int icId;
    private int carId;
    private Timestamp date;
    private float expenses;
    private float income;
}
