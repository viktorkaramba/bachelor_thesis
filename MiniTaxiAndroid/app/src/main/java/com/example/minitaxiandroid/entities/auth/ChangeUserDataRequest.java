package com.example.minitaxiandroid.entities.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeUserDataRequest {
    private String oldData;
    private String newData;
}
