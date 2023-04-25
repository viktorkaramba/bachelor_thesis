package com.unicyb.minitaxi.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    private int tokeId;
    private String token;
    private TokenType tokenType = TokenType.BEARER;
    private boolean revoked;
    private boolean expired;
    private int userId;
}
