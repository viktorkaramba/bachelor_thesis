package com.example.minitaxiandroid.entities.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverLocation {
    private int dlId;
    private int driverId;
    private String latitude;
    private String longitude;
}
