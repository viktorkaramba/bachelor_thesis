package com.example.energytaxiandroid.entities.userinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavouriteAddress {
    private int favouriteAddressId = 0;
    private int userId;
    private String address;
}
