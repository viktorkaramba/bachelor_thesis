package com.unicyb.minitaxi.entities.ranksystem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EliteRank {
    private int eliteRankId;
    private int rankId;
    private int ccId;
    private float period;
    private int freeOrders;
}
