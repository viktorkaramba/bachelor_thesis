package com.unicyb.minitaxi.entities.usersinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverSendInfoMessage {
    private int driverId;
    private String latitude;
    private String longitude;
}
