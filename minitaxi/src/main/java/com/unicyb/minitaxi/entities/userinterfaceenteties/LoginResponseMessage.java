package com.unicyb.minitaxi.entities.userinterfaceenteties;

import com.unicyb.minitaxi.entities.documents.ROLE;
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
