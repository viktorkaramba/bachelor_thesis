package com.unicyb.energytaxi.entities.ranksystem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRankAchievementInfo {
    private int uriId;
    private Timestamp dateUri;
    private int usersId;
    private int ranksId;
    private int numberOfUsesSale;
    private Timestamp deadlineDateSale;
}
