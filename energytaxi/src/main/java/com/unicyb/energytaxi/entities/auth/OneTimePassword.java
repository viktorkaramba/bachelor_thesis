package com.unicyb.energytaxi.entities.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OneTimePassword {
    private int otpId;
    private String userName;
    private String otPassword;
    private Timestamp otpDate;
}
