package com.unicyb.energytaxi.entities.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PricePerKilometersByTariff {
    private int ppkbtId;
    private int carClassId;
    private float morning;
    private float day;
    private float evening;
    private float night;
}
