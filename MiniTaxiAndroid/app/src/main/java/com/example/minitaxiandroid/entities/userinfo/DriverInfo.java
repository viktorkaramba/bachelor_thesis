package com.example.minitaxiandroid.entities.userinfo;

import com.example.minitaxiandroid.entities.document.CAR_CLASSES;
import com.example.minitaxiandroid.entities.document.DRIVER_STATUS;
import com.google.firebase.database.Exclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverInfo {
    private int driverId;
    private String driverFirstName;
    private String driverSurName;
    private String carProducer;
    private String carBrand;
    private CAR_CLASSES carClass;
    private double latitude;
    private double longitude;
    private DRIVER_STATUS status;

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("latitude", latitude);
        result.put("longitude", longitude);
        return result;
    }
}
