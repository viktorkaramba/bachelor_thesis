package com.example.minitaxiandroid.entities.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    private String accessToken;
    private String refreshToken;
    private int userId;
}
