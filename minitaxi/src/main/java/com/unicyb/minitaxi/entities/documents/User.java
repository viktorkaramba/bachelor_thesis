package com.unicyb.minitaxi.entities.documents;

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
    private int rankId;
    public User(String userName, String password, ROLE role, int rankId) {
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.rankId = rankId;
    }
}
