package com.unicyb.energytaxi.entities.indicators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavouriteAddress {
    private int favouriteAddressId;
    private int userId;
    private String address;
}
