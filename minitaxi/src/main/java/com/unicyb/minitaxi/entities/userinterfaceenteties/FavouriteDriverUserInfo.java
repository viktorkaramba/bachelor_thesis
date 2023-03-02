package com.unicyb.minitaxi.entities.userinterfaceenteties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavouriteDriverUserInfo {
    private int driverId;
    private int countOrders;
    private float averageRating;
    private String driverFirstName;
    private String driverSurName;
    private String carProducer;
    private String carBrand;
}
