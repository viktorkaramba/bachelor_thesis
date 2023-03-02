package com.unicyb.minitaxi.entities.indicators;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverCarRecommendations {
    private int dcrId;
    private Timestamp date;
    private int driverId;
    private int carId;
    private STATUS status;

    public DriverCarRecommendations(Timestamp date, int driverId, int carId, STATUS status) {
        this.date = date;
        this.driverId = driverId;
        this.carId = carId;
        this.status = status;
    }
}
