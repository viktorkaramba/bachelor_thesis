package com.unicyb.minitaxi.entities.indicators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavouriteDriver {
    private int favouriteDriverId = 0;
    private int driverId;
    private int userId;
}
