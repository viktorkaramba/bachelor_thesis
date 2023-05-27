package com.example.energytaxiandroid.entities.userinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavouriteAddressesUserInfo {
    private int favouriteAddressId;
    private int userId;
    private String address;
    private int count;
}
