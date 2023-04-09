package com.unicyb.minitaxi.entities.bonuses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MilitaryBonuses {
    private int militaryBonusesId;
    private int userId;
    private byte[] documentPhotoByteArray;
    private MILITARY_BONUS_STATUS militaryBonusStatus;
    private float saleValue;
    private Timestamp date;
}
