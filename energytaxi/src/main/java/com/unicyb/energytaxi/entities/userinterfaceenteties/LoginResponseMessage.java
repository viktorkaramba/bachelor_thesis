package com.unicyb.energytaxi.entities.userinterfaceenteties;

import com.unicyb.energytaxi.entities.documents.ROLE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseMessage {
    private String userId;
    private ROLE role;
    private int rankId;
}
