package com.unicyb.minitaxi.entities.usersinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStats {
    private int countOrders;
    private int countComments;
    private int rankId;
}
