package com.unicyb.energytaxi.services.other;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicyb.energytaxi.database.dao.auth.OneTimePasswordDAOImpl;
import com.unicyb.energytaxi.entities.auth.*;
import com.unicyb.energytaxi.entities.documents.Driver;
import com.unicyb.energytaxi.entities.documents.FullName;
import com.unicyb.energytaxi.entities.documents.ROLE;
import com.unicyb.energytaxi.entities.documents.User;
import com.unicyb.energytaxi.entities.indicators.STATUS;
import com.unicyb.energytaxi.entities.userinterfaceenteties.DriverResume;
import com.unicyb.energytaxi.entities.userinterfaceenteties.MyMessage;
import com.unicyb.energytaxi.entities.userinterfaceenteties.ResponseMessage;
import com.unicyb.energytaxi.services.documents.DriverService;
import com.unicyb.energytaxi.services.documents.FullNameService;
import com.unicyb.energytaxi.services.documents.UserService;
import com.unicyb.energytaxi.token.Token;
import com.unicyb.energytaxi.token.TokenType;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    @Autowired
    private UserService userService;

    @Autowired
    private FullNameService fullNameService;

    @Autowired
    private DriverService driverService;
    private final OneTimePasswordDAOImpl oneTimePasswordDAO = new OneTimePasswordDAOImpl();
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailForgotPasswordService emailForgotPasswordService;
    @Value("${application.security.opt.expiration}")
    private long optExpiration;
    @Autowired
    private TokenService tokenService;
    public RegisterResponse registerUser(UserRequest request){
        User user = User.builder()
                .userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(ROLE.USER)
                .rankId(1)
                .build();
        if(userService.add(user)){
            String jwtToken = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            saveUserToken(userService.getOneByUserName(user.getUsername()), jwtToken);
            return RegisterResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .userId(userService.getOneByUserName(user.getUsername()).getUserId())
                    .build();
        } else {
            return RegisterResponse.builder()
                    .accessToken("error")
                    .refreshToken("error")
                    .userId(0)
                    .build();
        }
    }

    public MyMessage registerDriver(DriverResume driverResume){
        MyMessage myMessage = null;
        try {
            User user = new User(driverResume.getDriverUserName(),
                    passwordEncoder.encode(driverResume.getDriverPassword()),
                    ROLE.DRIVER, 1);
            userService.add(user);
            int id = userService.getOneByUserName(driverResume.getDriverUserName()).getUserId();
            FullName fullName = new FullName(id, driverResume.getDriverFirstName(), driverResume.getDriverSurName(),
                    driverResume.getDriverPatronymic());
            fullNameService.add(fullName);
            Driver driver = new Driver(id, new Timestamp(new Date().getTime()), id, driverResume.getDriverTelephoneNumber(),
                    driverResume.getDriverExperience(), SalaryService.getSalary(driverResume.getDriverExperience()),
                    STATUS.WAITING, driverResume.getDriverUserName());
            driverService.add(driver);
            myMessage = new MyMessage("Resume added");
        }
        catch (Exception e){
            myMessage = new MyMessage("Error to add resume");
        }
        return myMessage;
    }

    public LoginResponse authenticateAdmin(UserRequest request){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                            request.getUserName(),
                            request.getPassword()
                    )
            );
        }
        catch (Exception e){
            return LoginResponse.builder()
                    .accessToken(e.getMessage())
                    .refreshToken(e.getMessage())
                    .user(null)
                    .build();
        }
        User user = userService.getOneByUserName(request.getUserName());
        if (user == null){
            throw new UsernameNotFoundException("User not found");
        }
        return getValidTokenLoginResponse(user);
    }

    public LoginResponse authenticateUser(UserRequest request, ROLE role){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                            request.getUserName(),
                            request.getPassword()
                    )
            );
        }
        catch (Exception e){
            return LoginResponse.builder()
                    .accessToken(e.getMessage())
                    .refreshToken(e.getMessage())
                    .user(null)
                    .build();
        }
        User user = userService.getOneByUserName(request.getUserName());
        if (user == null){
            throw new UsernameNotFoundException("User not found");
        }
        return authenticateAppUser(user, role);
    }


    public LoginResponse authenticateDriver(UserRequest request, ROLE role){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                            request.getUserName(),
                            request.getPassword()
                    )
            );
        }
        catch (Exception e){
            return LoginResponse.builder()
                    .accessToken(e.getMessage())
                    .refreshToken(e.getMessage())
                    .user(null)
                    .build();
        }
        User user = userService.getOneByUserName(request.getUserName());
        if (user == null){
            throw new UsernameNotFoundException("User not found");
        }
        if(!checkDriverResumeStatus(user.getUsername())){
            return LoginResponse.builder()
                    .accessToken("waiting answer")
                    .refreshToken("waiting answer")
                    .user(null)
                    .build();
        }
        return authenticateAppUser(user, role);
    }

    private boolean checkDriverResumeStatus(String username) {
        Driver driver = driverService.getByDriverUserName(username);
        if(driver.getResumeStatus().equals(STATUS.WAITING)){
            return false;
        }
        else {
            return true;
        }
    }

    public LoginResponse authenticateAppUser(User user, ROLE role){
        if(user.getRole() != role){
            if(role.equals(ROLE.USER)){
                return LoginResponse.builder()
                        .accessToken("You are not a user")
                        .refreshToken("You are not a user")
                        .user(null)
                        .build();
            } else if (role.equals(ROLE.DRIVER)) {
                return LoginResponse.builder()
                        .accessToken("You are not a driver")
                        .refreshToken("You are not a driver")
                        .user(null)
                        .build();
            }
        }
        return getValidTokenLoginResponse(user);
    }

    private LoginResponse getValidTokenLoginResponse(User user) {
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return LoginResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .user(user)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .userId(user.getUserId())
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenService.add(token);
    }
    private void revokeAllUserTokens(User user) {
        tokenService.revokeAllUserTokens(user.getUserId());
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        System.out.println("request: " + request.getServletPath());
        final String refreshToken = extractJwtFromRequest(request);
        System.out.println("refreshToken: " + refreshToken);
        if (refreshToken == null) {
            return;
        }
        try {
            final String userName = jwtService.extractUsername(refreshToken);
            System.out.println(userName);
            if (userName != null) {
                var user = userService.getOneByUserName(userName);
                if(user == null){
                    var authResponse = RegisterResponse.builder()
                            .accessToken("User not found")
                            .refreshToken("User not found")
                            .build();
                    new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
                }
                else {
                    if (jwtService.isTokenValid(refreshToken, user)) {
                        var accessToken = jwtService.generateToken(user);
                        revokeAllUserTokens(user);
                        saveUserToken(user, accessToken);
                        var authResponse = RegisterResponse.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .build();
                        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
                    }
                }
            }
        }catch (ExpiredJwtException e){
            var authResponse = RegisterResponse.builder()
                    .accessToken("Token expired")
                    .refreshToken("Token expired")
                    .build();
            new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
        }
    }

    public ResponseMessage checkToken(HttpServletRequest request){
        String accessToken = extractJwtFromRequest(request);
        if (accessToken == null) {
            return new ResponseMessage("error", "No token");
        }
        try {
            String userName = jwtService.extractUsername(accessToken);
            if (userName != null) {
                var user = userService.getOneByUserName(userName);
                if(user == null){
                    return new ResponseMessage("error", "User not found");
                }
                Token token = tokenService.getByToken(accessToken);
                boolean isValidToken = false;
                if(!token.isExpired() && !token.isRevoked()){
                    isValidToken = true;
                }
                if (jwtService.isTokenValid(accessToken, user) && isValidToken) {
                    return new ResponseMessage(String.valueOf(user.getRankId()),
                            "Token is valid");
                }
                else {
                    return new ResponseMessage("error",
                            "Token is not valid");
                }
            }
        }catch (ExpiredJwtException e){
            return new ResponseMessage("error", "Token expired");
        }
        return null;
    }

    public MyMessage changePassword(ChangeUserDataRequest changeUserDataRequest, HttpServletRequest request) {
        final String accessToken = extractJwtFromRequest(request);
        if (accessToken == null) {
            return new MyMessage("No token");
        }
        try {
            String userName = jwtService.extractUsername(accessToken);
            if (userName != null) {
                User user = userService.getOneByUserName(userName);
                if(user == null){
                    return new MyMessage("User not found");
                }
                if (!passwordEncoder.matches(changeUserDataRequest.getOldData(), user.getPassword())) {
                    return new MyMessage("Old password is invalid");
                }
                user.setPassword(passwordEncoder.encode(changeUserDataRequest.getNewData()));
                if(!userService.update(user)){
                    return new MyMessage("Error to change password");
                }
            }
        }catch (ExpiredJwtException e){
            return new MyMessage("Token expired");
        }
        return new MyMessage("Password changed");
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public LoginResponse changeUserName(MyMessage myMessage, HttpServletRequest request) {
        final String accessToken = extractJwtFromRequest(request);
        if (accessToken == null) {
            return LoginResponse.builder()
                    .accessToken("No token")
                    .refreshToken("No token")
                    .user(null)
                    .build();
        }
        try {
            String userName = jwtService.extractUsername(accessToken);
            if (userName != null) {
                User user = userService.getOneByUserName(userName);
                if(user == null){
                    return LoginResponse.builder()
                            .accessToken("User not found")
                            .refreshToken("User not found")
                            .user(null)
                            .build();
                }
                user.setUserName(myMessage.getContent());
                if(userService.update(user)){
                    if(user.getRole().equals(ROLE.DRIVER)){
                        boolean q = driverService.updateByUsername(userName, user.getUsername());
                        System.out.println(q);
                        if(!q){
                            return LoginResponse.builder()
                                    .accessToken("error")
                                    .refreshToken("error")
                                    .user(null)
                                    .build();
                        }
                    }
                    String jwtToken = jwtService.generateToken(user);
                    String refreshToken = jwtService.generateRefreshToken(user);
                    revokeAllUserTokens(user);
                    saveUserToken(user, jwtToken);
                    log.info("Username changed");
                    return LoginResponse.builder()
                            .accessToken(jwtToken)
                            .refreshToken(refreshToken)
                            .user(user)
                            .build();
                } else {
                    return LoginResponse.builder()
                            .accessToken("error")
                            .refreshToken("error")
                            .user(null)
                            .build();
                }
            }
        }catch (ExpiredJwtException e){
            return LoginResponse.builder()
                    .accessToken("Token is expired")
                    .refreshToken("Token is expired")
                    .user(null)
                    .build();
        }
        return null;
    }

    public MyMessage resetPassword(ChangeUserDataRequest changeUserDataRequest) {
        String userName = changeUserDataRequest.getOldData();
        try {
            User user = userService.getOneByUserName(userName);
            if(user == null){
                return new MyMessage("User with this username not found");
            }
            else {
                emailForgotPasswordService.sendOTPCode(changeUserDataRequest.getNewData());
                oneTimePasswordDAO.add(new OneTimePassword(-1, userName,
                        passwordEncoder.encode(emailForgotPasswordService.getOPT()),
                        new Timestamp(new Date().getTime())));
                return new MyMessage("Code send to email");
            }

        } catch (MessagingException e) {
            return new MyMessage("Error to send code to email");
        }
    }

    public LoginResponse checkOPT(ChangeUserDataRequest changeUserDataRequest) {
        OneTimePassword oneTimePassword = oneTimePasswordDAO.getOneByUserName(changeUserDataRequest.getOldData());
        if(passwordEncoder.matches(changeUserDataRequest.getNewData(), oneTimePassword.getOtPassword())){
           if(oneTimePassword.getOtpDate().getTime() + optExpiration < System.currentTimeMillis()){
               oneTimePasswordDAO.deleteByUserName(changeUserDataRequest.getOldData());
               return LoginResponse.builder()
                       .accessToken("Code expired")
                       .refreshToken("Code expired")
                       .user(null).build();
           }
           else {
               User user = userService.getOneByUserName(changeUserDataRequest.getOldData());
               String jwtToken = jwtService.generateToken(user);
               String refreshToken = jwtService.generateRefreshToken(user);
               revokeAllUserTokens(user);
               saveUserToken(user, jwtToken);
               oneTimePasswordDAO.deleteByUserName(changeUserDataRequest.getOldData());
               return LoginResponse.builder()
                       .accessToken(jwtToken)
                       .refreshToken(refreshToken)
                       .user(user).build();
           }
        }
        else{
            return LoginResponse.builder()
                    .accessToken("Incorrect code")
                    .refreshToken("Incorrect code")
                    .user(null).build();
        }
    }
}
