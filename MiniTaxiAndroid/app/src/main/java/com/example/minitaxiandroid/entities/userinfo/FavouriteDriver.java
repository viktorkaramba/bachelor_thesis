package com.example.minitaxiandroid.entities.userinfo;

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
