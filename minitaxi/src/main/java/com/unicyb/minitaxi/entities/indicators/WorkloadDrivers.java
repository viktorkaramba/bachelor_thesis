package com.unicyb.minitaxi.entities.indicators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkloadDrivers {
    private int wldId;
    private int driverId;
    private Timestamp date;
    private int morning;
    private int day;
    private int evening;
    private int night;
}
