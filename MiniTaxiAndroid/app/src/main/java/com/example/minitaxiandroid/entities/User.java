package com.example.minitaxiandroid.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int userId;
    private String userName;
    private String password;
    private ROLE role;

    public User(String userName, String password, ROLE role) {
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

}
