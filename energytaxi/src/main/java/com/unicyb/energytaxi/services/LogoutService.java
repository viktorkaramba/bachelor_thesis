package com.unicyb.energytaxi.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicyb.energytaxi.entities.auth.RegisterResponse;
import com.unicyb.energytaxi.token.TokenDAOImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Service
@RequiredArgsConstructor
@CrossOrigin
public class LogoutService implements LogoutHandler {

    private final TokenDAOImpl tokenDAO = new TokenDAOImpl();
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        System.out.println(authHeader);
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        if(tokenDAO.revokeOneToken(jwt)){
            RegisterResponse authResponse = RegisterResponse.builder()
                    .accessToken("successfully logout")
                    .refreshToken("successfully logout")
                    .build();
            try {
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else{
            System.out.println("error");
            response.setHeader("error", "Token expired");
            response.setStatus(FORBIDDEN.value());
            Map<String, String> error = new HashMap<>();
            error.put("error_message",  "Token expired");
            response.setContentType(APPLICATION_JSON_VALUE);
            tokenDAO.deleteByToken(jwt);
            try {
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
