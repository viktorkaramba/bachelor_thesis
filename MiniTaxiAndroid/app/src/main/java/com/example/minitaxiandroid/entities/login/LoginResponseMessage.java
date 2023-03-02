package com.example.minitaxiandroid.entities.login;

import com.example.minitaxiandroid.entities.ROLE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseMessage {
    private String userId;
    private ROLE role;
}