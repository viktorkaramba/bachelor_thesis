package com.example.energytaxiandroid.entities.userinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavouriteDriverUserInfo {
    private int favouriteDriverId;
    private int driverId;
    private int countOrders;
    private float averageRating;
    private String driverFirstName;
    private String driverSurName;
    private String carProducer;
    private String carBrand;
}
