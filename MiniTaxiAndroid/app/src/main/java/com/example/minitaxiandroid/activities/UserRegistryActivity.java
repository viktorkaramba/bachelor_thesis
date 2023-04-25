package com.example.minitaxiandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.minitaxiandroid.R;
import com.example.minitaxiandroid.api.MiniTaxiApi;
import com.example.minitaxiandroid.api.RetrofitService;
import com.example.minitaxiandroid.entities.auth.RegisterResponse;
import com.example.minitaxiandroid.entities.auth.UserRequest;
import com.example.minitaxiandroid.services.UserInfoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegistryActivity extends AppCompatActivity {

    private EditText userName;
    private EditText password;
    private Button registry;
    private TextView registerRegisterTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registry);
        userName = findViewById(R.id.userNameUserRegistryEditText);
        password = findViewById(R.id.passwordUserRegistryEditText);
        registry = findViewById(R.id.registryLoginButton);
        registerRegisterTextView = findViewById(R.id.registerRegisterTextView);
        registerRegisterTextView.setOnClickListener(view -> goLogin());
        UserInfoService.init(UserRegistryActivity.this);
        registry.setOnClickListener(view -> registry());
    }

    public void registry(){
        UserInfoService.addProperty("username", userName.getText().toString());
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi userRegisterApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        if(isValidInfo()){
            UserRequest userRequest = new UserRequest(userName.getText().toString(), password.getText().toString());
            userRegisterApi.userRegister(userRequest)
                    .enqueue(new Callback<RegisterResponse>() {
                        @Override
                        public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                            if(response.body() != null){
                                RegisterResponse registerResponse = response.body();
                                if(registerResponse.getAccessToken().equals("error")
                                        && registerResponse.getRefreshToken().equals("error")){
                                    userName.setError(getResources().getString(R.string.user_already_exist));
                                }
                                else {
                                    UserInfoService.addProperty("access_token", registerResponse.getAccessToken());
                                    UserInfoService.addProperty("refresh_token", registerResponse.getRefreshToken());
                                    UserInfoService.addProperty("userId", String.valueOf(registerResponse.getUserId()));
                                    UserInfoService.addProperty("rankId", "1");
                                    goMain();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<RegisterResponse> call, Throwable t) {
                            UserRegistryActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(UserRegistryActivity.this, "Failed to register. Try again later",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });
        }
    }

    private void goMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void goLogin() {
        Intent intent = new Intent(this, UserLoginActivity.class);
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