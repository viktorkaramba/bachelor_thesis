package com.example.minitaxiandroid.entities.ranks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEliteRankAchievementInfo {
    private int ueraiId;
    private Timestamp dateUerai;
    private int usersId;
    private int eliteRanksId;
    private int numberOfUsesFreeOrder;
    private Timestamp deadlineDateFreeOrder;
    private int carClassId;
}
