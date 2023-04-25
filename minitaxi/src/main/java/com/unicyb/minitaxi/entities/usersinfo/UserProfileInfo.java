package com.unicyb.minitaxi.entities.usersinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileInfo {
    private String userName;
    private String rankName;
    private String isMilitaryBonuses;
}
