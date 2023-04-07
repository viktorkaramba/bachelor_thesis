package com.example.minitaxiandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.minitaxiandroid.R;
import com.example.minitaxiandroid.entities.ROLE;
import com.example.minitaxiandroid.entities.User;
import com.example.minitaxiandroid.entities.login.LoginResponseMessage;
import com.example.minitaxiandroid.services.UserLoginInfoService;
import com.example.minitaxiandroid.websocket.WebSocketClient;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.concurrent.ListenableFuture;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static com.example.minitaxiandroid.services.ObjectParserService.parseLoginMessageFromString;

public class UserLoginActivity extends AppCompatActivity {

    private EditText userName;
    private EditText password;
    private CheckBox checkBox;
    private StompSession stompSession;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        userName = findViewById(R.id.userNameLoginEditText);
        password = findViewById(R.id.passwordLoginEditText);
        checkBox = findViewById(R.id.driverUserCheckBox);
        Button login = findViewById(R.id.loginUserLoginButton);
        Button registryAsDriver = findViewById(R.id.registryDriverLoginButton);
        Button registryAsUser = findViewById(R.id.registryUserLoginButton);
        UserLoginInfoService.init(UserLoginActivity.this);
        //TODO
        UserLoginInfoService.addProperty("isLogin",  "True");
        UserLoginInfoService.addProperty("userId", "1");
        UserLoginInfoService.addProperty("username", "viktor2002");
        UserLoginInfoService.addProperty("password", "driver");

        if(checkIfLogin()){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else {
            login.setOnClickListener(view -> login());
        }
        registryAsDriver.setOnClickListener(view -> goRegistryAsDriver());
        registryAsUser.setOnClickListener(view -> goRegistryAsUser());
    }

    public boolean checkIfLogin(){
        if (Objects.equals(UserLoginInfoService.getProperty("isLogin"), "True")){
            return true;
        }
        else {
            return false;
        }
    }

    public void goRegistryAsDriver(){
        Intent intent = new Intent(this, DriverRegistrationActivity.class);
        startActivity(intent);
    }

    public void goRegistryAsUser(){
        Intent intent = new Intent(this, UserRegistryActivity.class);
        startActivity(intent);
    }

    public void login(){
        new Thread(() -> {
            try {
                Log.d("MakeOrder", "Run");
                WebSocketClient userClient = new WebSocketClient();
                ListenableFuture<StompSession> f = userClient.connect();
                stompSession = f.get();
                if(checkBox.isChecked()){
                    user = new User(-1, userName.getText().toString(), password.getText().toString(),
                            ROLE.DRIVER, 0);
                }
                else {
                    user = new User(-1, userName.getText().toString(), password.getText().toString(),
                            ROLE.USER, 0);
                    Log.d("UserLogin", user.getUserName());
                }
                subscribeAuthorization(stompSession);
                sendLoginMessage(stompSession);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void subscribeAuthorization(StompSession stompSession) throws ExecutionException, InterruptedException {
        stompSession.subscribe("/user/" + user.getUserName() + "/authorization", new StompFrameHandler() {
            public Type getPayloadType(StompHeaders stompHeaders) {
                return byte[].class;
            }

            public void handleFrame(StompHeaders stompHeaders, Object o) {
                String response =  new String((byte[]) o);
                LoginResponseMessage loginResponseMessage = null;
                try {
                    loginResponseMessage = parseLoginMessageFromString(response);
                    getResponse(loginResponseMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void sendLoginMessage(StompSession stompSession) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{ \"userId\" : ").append(user.getUserId()).append(",")
                .append("\"userName\" : \"").append(user.getUserName()).append("\",")
                .append("\"password\" : \"").append(user.getPassword()).append("\",")
                .append("\"role\" : \"").append(user.getRole()).append("\",")
                .append("\"rankId\" : \"").append(user.getRankId()).append("\" }");
        stompSession.send("/app/user-authorization", stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
    }

    private void getResponse(LoginResponseMessage loginResponseMessage) {
        if(loginResponseMessage.getUserId().equals("Incorrect password")){
            runOnUiThread(() -> Toast.makeText(UserLoginActivity.this, "Incorrect password",
                    Toast.LENGTH_SHORT).show());

        }
        else if(loginResponseMessage.getUserId().equals("Need registry")){
            runOnUiThread(() -> Toast.makeText(UserLoginActivity.this, "Need to registry",
                    Toast.LENGTH_SHORT).show());

        }
        else if(loginResponseMessage.getUserId().equals("User is not driver")){
            runOnUiThread(() -> Toast.makeText(UserLoginActivity.this, "User is not driver",
                    Toast.LENGTH_SHORT).show());
        }
        else{
            try {
                Intent intent;
                if(loginResponseMessage.getRole().name().equals(ROLE.DRIVER.name())) {
                    intent = new Intent(UserLoginActivity.this, DriverMenuActivity.class);
                }
                else {
                    intent = new Intent(UserLoginActivity.this, MainActivity.class);
                }
                intent.putExtra("userId", loginResponseMessage.getUserId());
                UserLoginInfoService.addProperty("isLogin",  "True");
                UserLoginInfoService.addProperty("userId",  loginResponseMessage.getUserId());
                UserLoginInfoService.addProperty("username", userName.getText().toString());
                UserLoginInfoService.addProperty("password", password.getText().toString());
                UserLoginInfoService.addProperty("rankId", String.valueOf(loginResponseMessage.getRankId()));
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}