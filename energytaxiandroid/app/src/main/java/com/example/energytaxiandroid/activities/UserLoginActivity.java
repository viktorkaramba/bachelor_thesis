package com.example.energytaxiandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.energytaxiandroid.R;
import com.example.energytaxiandroid.api.MiniTaxiApi;
import com.example.energytaxiandroid.api.RetrofitService;
import com.example.energytaxiandroid.entities.auth.LoginResponse;
import com.example.energytaxiandroid.entities.auth.UserRequest;
import com.example.energytaxiandroid.services.DriverInfoService;
import com.example.energytaxiandroid.services.UserInfoService;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLoginActivity extends AppCompatActivity {

    private EditText userName;
    private EditText password;
    private CheckBox checkBox;
    private TextView loginRegisterTextView, loginForgotPasswordTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        userName = findViewById(R.id.userNameLoginEditText);
        password = findViewById(R.id.passwordLoginEditText);
        checkBox = findViewById(R.id.driverUserCheckBox);
        loginRegisterTextView = findViewById(R.id.loginRegisterTextView);
        loginForgotPasswordTextView = findViewById(R.id.loginForgotPasswordTextView);
        loginRegisterTextView.setOnClickListener(view -> goRegistryAsUser());
        loginForgotPasswordTextView.setOnClickListener(view -> resetPassword());
        Button login = findViewById(R.id.loginUserLoginButton);
        login.setOnClickListener(view -> login());
        UserInfoService.init(UserLoginActivity.this);
        DriverInfoService.init(UserLoginActivity.this);
    }

    private void resetPassword() {
//        RetrofitService retrofitService = new RetrofitService();
//        MiniTaxiApi api = retrofitService.getRetrofit().create(MiniTaxiApi.class);
//        api.resetPassword()
//                .enqueue(new Callback<List<FavouriteAddressesUserInfo>>() {
//                    @Override
//                    public void onResponse(@NotNull Call<List<FavouriteAddressesUserInfo>> call,
//                                           @NotNull Response<List<FavouriteAddressesUserInfo>> response) {
//                        assert response.body() != null;
//                        parseFavouriteAddressesList(response.body());
//                    }
//
//                    @Override
//                    public void onFailure(@NotNull Call<List<FavouriteAddressesUserInfo>> call, @NotNull Throwable t) {
//                        Toast.makeText(MainActivity.this,
//                                "Failed to load favourite drivers info",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

    public void goRegistryAsUser(){
        Intent intent = new Intent(this, UserRegistryActivity.class);
        startActivity(intent);
    }

    public void login(){
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi api = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        if(isValidInfo()){
            if(!checkBox.isChecked()) {
                api.authenticateUser(new UserRequest(userName.getText().toString(), password.getText().toString()))
                        .enqueue(new Callback<LoginResponse>() {
                            @Override
                            public void onResponse(@NotNull Call<LoginResponse> call,
                                                   @NotNull Response<LoginResponse> response) {
                                if (response.body() != null) {
                                    LoginResponse loginResponse = response.body();
                                    if (loginResponse.getAccessToken().equals(getResources().getString(R.string.bad_credentials))) {
                                        userName.setError(getResources().getString(R.string.invalid_user_credentials));
                                        password.setError(getResources().getString(R.string.invalid_user_credentials));
                                    } else if (loginResponse.getAccessToken().equals(getResources().getString(R.string.not_user))) {
                                        Toast.makeText(UserLoginActivity.this,
                                                "Failed to login. You are not a user",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        System.out.println(loginResponse);
                                        UserInfoService.addProperty("access_token", loginResponse.getAccessToken());
                                        UserInfoService.addProperty("refresh_token", loginResponse.getRefreshToken());
                                        UserInfoService.addProperty("userId", String.valueOf(loginResponse.getUser().getUserId()));
                                        UserInfoService.addProperty("username", loginResponse.getUser().getUsername());
                                        System.out.println("username: " + loginResponse.getUser().getUsername());
                                        UserInfoService.addProperty("rankId", String.valueOf(loginResponse.getUser().getRankId()));
                                        goMain();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NotNull Call<LoginResponse> call, @NotNull Throwable t) {
                                UserLoginActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(UserLoginActivity.this,
                                                "Failed to login",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
            }
            else {
                api.authenticateDriver(new UserRequest(userName.getText().toString(), password.getText().toString()))
                        .enqueue(new Callback<LoginResponse>() {
                            @Override
                            public void onResponse(@NotNull Call<LoginResponse> call,
                                                   @NotNull Response<LoginResponse> response) {
                                if (response.body() != null) {
                                    LoginResponse loginResponse = response.body();
                                    if (loginResponse.getAccessToken().equals(getResources().getString(R.string.bad_credentials))) {
                                        userName.setError(getResources().getString(R.string.invalid_user_credentials));
                                        password.setError(getResources().getString(R.string.invalid_user_credentials));
                                    }
                                    else if (loginResponse.getAccessToken().equals(getResources().getString(R.string.not_driver))) {
                                        Toast.makeText(UserLoginActivity.this,
                                                "Failed to login. You are not a driver",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    else if (loginResponse.getAccessToken().equals(getResources().getString(R.string.waiting_answer))) {
                                        Toast.makeText(UserLoginActivity.this,
                                                "Please wait until an administrator checks your resume",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        DriverInfoService.addProperty("access_token", loginResponse.getAccessToken());
                                        DriverInfoService.addProperty("refresh_token", loginResponse.getRefreshToken());
                                        DriverInfoService.addProperty("driverId", String.valueOf(loginResponse.getUser().getUserId()));
                                        DriverInfoService.addProperty("username", loginResponse.getUser().getUsername());
                                        goDriverMenu();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NotNull Call<LoginResponse> call, @NotNull Throwable t) {
                                UserLoginActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(UserLoginActivity.this,
                                                "Failed to login",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        });
            }
        }
    }

    private void goMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void goDriverMenu() {
        Intent intent = new Intent(this, DriverMenuActivity.class);
        startActivity(intent);
    }

    private boolean isValidInfo(){
        if(userName.getText().toString().length() < 4 || userName.getText().toString().length() > 16){
            userName.setError(getResources().getString(R.string.user_name_to_small));
            return false;
        }
        if(userName.getText().toString().length() == 0){
            userName.setError(getResources().getString(R.string.username_empty));
            return false;
        }
        if(password.getText().toString().length() < 8 || userName.getText().toString().length() > 20){
            password.setError(getResources().getString(R.string.user_password_to_small));
            return false;
        }
        if(password.getText().toString().length() == 0){
            password.setError(getResources().getString(R.string.user_password_empty));
            return false;
        }
        return true;
    }
}