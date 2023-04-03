package com.unicyb.minitaxi.entities.userinterfaceenteties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAddressesMessage {
    private int userId;
    private String userAddressFrom;
    private String userAddressTo;
}
