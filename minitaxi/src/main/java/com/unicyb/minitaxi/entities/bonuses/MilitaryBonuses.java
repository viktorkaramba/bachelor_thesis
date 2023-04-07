package com.unicyb.minitaxi.entities.bonuses;

import com.unicyb.minitaxi.entities.indicators.STATUS;
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
    private STATUS status;
    private float saleValue;
    private Timestamp date;
}
