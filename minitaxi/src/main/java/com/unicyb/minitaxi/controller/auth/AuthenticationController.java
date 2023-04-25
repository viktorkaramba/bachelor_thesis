package com.unicyb.minitaxi.controller.auth;

import com.unicyb.minitaxi.entities.auth.ChangeUserDataRequest;
import com.unicyb.minitaxi.entities.auth.LoginResponse;
import com.unicyb.minitaxi.entities.auth.RegisterResponse;
import com.unicyb.minitaxi.entities.auth.UserRequest;
import com.unicyb.minitaxi.entities.documents.ROLE;
import com.unicyb.minitaxi.entities.userinterfaceenteties.DriverResume;
import com.unicyb.minitaxi.entities.userinterfaceenteties.MyMessage;
import com.unicyb.minitaxi.entities.userinterfaceenteties.ResponseMessage;
import com.unicyb.minitaxi.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    @PostMapping("/user-register")
    public ResponseEntity<RegisterResponse> register(@RequestBody UserRequest request){
        return ResponseEntity.ok(authenticationService.registerUser(request));
    }

    @PostMapping("/driver-register")
    public ResponseEntity<MyMessage> registerDriver(@RequestBody DriverResume driverResume){
        return ResponseEntity.ok(authenticationService.registerDriver(driverResume));
    }


    @PostMapping("/user-authenticate")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody UserRequest request){
        return ResponseEntity.ok(authenticationService.authenticateUser(request, ROLE.USER));
    }

    @PostMapping("/driver-authenticate")
    public ResponseEntity<LoginResponse> authenticateDriver(@RequestBody UserRequest request){
        return ResponseEntity.ok(authenticationService.authenticateDriver(request,  ROLE.DRIVER));
    }

    @PostMapping("/check-token")
    public ResponseEntity<ResponseMessage> checkToken(HttpServletRequest request){
        return ResponseEntity.ok(authenticationService.checkToken(request));
    }

    @PostMapping("/change-password")
    public ResponseEntity<MyMessage> changePassword(@RequestBody ChangeUserDataRequest changeUserDataRequest,
                                                  HttpServletRequest request){
        return ResponseEntity.ok(authenticationService.changePassword(changeUserDataRequest, request));
    }

    @PostMapping("/change-username")
    public ResponseEntity<LoginResponse> changeUsername(@RequestBody MyMessage message,
                                                  HttpServletRequest request){
        return ResponseEntity.ok(authenticationService.changeUserName(message, request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MyMessage> resetPassword(@RequestBody ChangeUserDataRequest changeUserDataRequest){
        return ResponseEntity.ok(authenticationService.resetPassword(changeUserDataRequest));
    }

    @PostMapping("/reset-password-opt")
    public ResponseEntity<LoginResponse> checkOPT(@RequestBody ChangeUserDataRequest changeUserDataRequest){
        return ResponseEntity.ok(authenticationService.checkOPT(changeUserDataRequest));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }
}
