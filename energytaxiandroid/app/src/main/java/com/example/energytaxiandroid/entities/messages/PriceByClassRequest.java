package com.example.energytaxiandroid.entities.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceByClassRequest {
    private int userId;
    private String addressFrom;
    private String addressTo;
    private float distance;
}
