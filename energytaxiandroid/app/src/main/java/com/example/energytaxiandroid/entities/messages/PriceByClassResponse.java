package com.example.energytaxiandroid.entities.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceByClassResponse {
    private List<Float> priceByClass;
    private boolean isMilitaryBonus = false;
    private String className;
}
