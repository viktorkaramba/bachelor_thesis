package com.unicyb.energytaxi.entities.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarUpdateRequest {
    private int newCarId;
    private int oldCarId;
}
