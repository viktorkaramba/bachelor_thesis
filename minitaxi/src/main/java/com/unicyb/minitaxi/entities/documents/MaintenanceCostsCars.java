package com.unicyb.minitaxi.entities.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceCostsCars {
    private int mccId;
    private Timestamp date;
    private String type;
    private float costs;
    private int carId;
}
