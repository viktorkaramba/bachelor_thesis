package com.unicyb.minitaxi.ranksystem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EliteRankUserInfo {
    private int ranksId;
    private String name;
    private int minOrders;
    private int minComments;
    private float salePeriod;
    private float saleValue;
    private String carClassName;
    private float period;
    private int freeOrders;
}
