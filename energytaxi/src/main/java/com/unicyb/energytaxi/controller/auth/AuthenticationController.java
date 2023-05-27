package com.unicyb.energytaxi.controller.auth;

import com.unicyb.energytaxi.entities.auth.ChangeUserDataRequest;
import com.unicyb.energytaxi.entities.auth.LoginResponse;
import com.unicyb.energytaxi.entities.auth.RegisterResponse;
import com.unicyb.energytaxi.entities.auth.UserRequest;
import com.unicyb.energytaxi.entities.documents.ROLE;
import com.unicyb.energytaxi.entities.userinterfaceenteties.DriverResume;
import com.unicyb.energytaxi.entities.userinterfaceenteties.MyMessage;
import com.unicyb.energytaxi.entities.userinterfaceenteties.ResponseMessage;
import com.unicyb.energytaxi.services.other.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;
    @PostMapping("/user-register")
    public ResponseEntity<RegisterResponse> register(@RequestBody UserRequest request){
        return ResponseEntity.ok(authenticationService.registerUser(request));
    }

    @PostMapping("/driver-register")
    public ResponseEntity<MyMessage> registerDriver(@RequestBody DriverResume driverResume){
        return ResponseEntity.ok(authenticationService.registerDriver(driverResume));
    }

    @PostMapping("/admin-authenticate")
    public ResponseEntity<LoginResponse> authenticateAdmin(@RequestBody UserRequest request){
        return ResponseEntity.ok(authenticationService.authenticateAdmin(request));
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
