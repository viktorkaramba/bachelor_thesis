package com.example.energytaxiandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.energytaxiandroid.R;
import com.example.energytaxiandroid.api.MiniTaxiApi;
import com.example.energytaxiandroid.api.RetrofitService;
import com.example.energytaxiandroid.entities.auth.ChangeUserDataRequest;
import com.example.energytaxiandroid.entities.auth.LoginResponse;
import com.example.energytaxiandroid.entities.auth.RegisterResponse;
import com.example.energytaxiandroid.entities.messages.MyMessage;
import com.example.energytaxiandroid.entities.userinfo.UserProfileInfo;
import com.example.energytaxiandroid.services.UserInfoService;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

public class UserProfileActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView userProfileNameEditText, userProfilePasswordEditText, userProfileRankEditText;
    private EditText newUserNameEditTextTextPersonName, currentPasswordEditTextPassword, newPasswordEditTextPassword;
    private ImageView userProfileChangeUsername, userProfileChangePassword;
    private Button newUserNameSentButton, newPasswordSentButton, favouriteAddressBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        UserInfoService.init(UserProfileActivity.this);
        getUserProfileInfo();
        initializeViews();
    }

    private void getUserProfileInfo() {
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi api = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        api.getUserProfileInfo( "Bearer " +  UserInfoService.getProperty("access_token"),
                        UserInfoService.getProperty("userId"))
                .enqueue(new Callback<UserProfileInfo>() {
                    @Override
                    public void onResponse(@NotNull Call<UserProfileInfo> call,
                                           @NotNull Response<UserProfileInfo> response) {

                        if(response.body() != null){
                            try {
                                if(response.errorBody() != null){
                                    if(response.errorBody().string().contains(getResources()
                                            .getString(R.string.token_expired))){
                                        refreshToken();
                                    }
                                }
                                else {
                                    UserProfileInfo userProfileInfo = response.body();
                                    userProfileNameEditText.setText(userProfileInfo.getUserName());
                                    userProfileRankEditText.setText(userProfileInfo.getRankName());
                                    if(userProfileInfo.getIsMilitaryBonuses() != null){
                                        userProfileRankEditText.setVisibility(View.VISIBLE);
                                        userProfileRankEditText.setText(getResources().getString(R.string.user_profile_military_bonus));
                                    }
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<UserProfileInfo> call, @NotNull Throwable t) {
                        System.out.println(t.getMessage());
                        UserProfileActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(UserProfileActivity.this,
                                        "Failed to change username",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
    }

    private void initializeViews() {
        userProfileNameEditText = findViewById(R.id.userProfileNameEditText);
        userProfilePasswordEditText = findViewById(R.id.userProfilePasswordEditText);
        userProfileRankEditText = findViewById(R.id.userProfileRankEditText);
        userProfileChangeUsername = findViewById(R.id.userProfileChangeUsername);
        favouriteAddressBackButton = findViewById(R.id.favouriteAddressBackButton);
        userProfileChangeUsername.setOnClickListener(view -> {
            showChangeUserNameDialog();
        });
        userProfileChangePassword = findViewById(R.id.userProfileChangePassword);
        userProfileChangePassword.setOnClickListener(view -> {
            showChangePasswordDialog();
        });
        favouriteAddressBackButton.setOnClickListener(view -> goMain());
    }

    private void showChangeUserNameDialog() {
        dialogBuilder =new AlertDialog.Builder(UserProfileActivity.this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.user_change_username, null);
        initializeChangeUserName(contactPopupView);
        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.card_view_border_reverse, getTheme()));
        dialog.show();
    }

    public void initializeChangeUserName(View contactPopupView){
        newUserNameEditTextTextPersonName = contactPopupView.findViewById(R.id.newUserNameEditTextTextPersonName);
        newUserNameSentButton = contactPopupView.findViewById(R.id.newUserNameSentButton);
        newUserNameSentButton.setOnClickListener(view -> sentNewUserName());
    }

    private void showChangePasswordDialog() {
        dialogBuilder =new AlertDialog.Builder(UserProfileActivity.this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.user_change_password, null);
        initializeChangePassword(contactPopupView);
        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.card_view_border_reverse, getTheme()));
        dialog.show();
    }

    public void initializeChangePassword(View contactPopupView){
        currentPasswordEditTextPassword = contactPopupView.findViewById(R.id.currentPasswordEditTextPassword);
        newPasswordEditTextPassword = contactPopupView.findViewById(R.id.newPasswordEditTextPassword);
        newPasswordSentButton = contactPopupView.findViewById(R.id.newPasswordSentButton);
        newPasswordSentButton.setOnClickListener(view -> sentNewPassword());
    }

    private void sentNewUserName() {
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi api = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        if(isUserNameValid()) {
//            api.changeUserName("Bearer " + InfoService.getProperty("access_token"),
            api.changeUserName("Bearer " + UserInfoService.getProperty("access_token"),
                            new MyMessage(newUserNameEditTextTextPersonName.getText().toString()))
                    .enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(@NotNull Call<LoginResponse> call,
                                               @NotNull Response<LoginResponse> response) {
                            if(response.body() != null){
                                LoginResponse loginResponse = response.body();
                                if(loginResponse.getAccessToken().equals("error")){
                                    newUserNameEditTextTextPersonName.setError(getResources()
                                            .getString(R.string.user_already_exist));
                                }
                                else if(loginResponse.getAccessToken().equals(getResources()
                                        .getString(R.string.token_expired))){
                                    refreshToken();
                                }
                                else{
                                    goLogin();
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<LoginResponse> call, @NotNull Throwable t) {
                            Toast.makeText(UserProfileActivity.this,
                                    "Failed to change username",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private boolean isUserNameValid() {
        if(newUserNameEditTextTextPersonName.getText().toString().length() < 4
                || newUserNameEditTextTextPersonName.getText().toString().length() > 16){
            newUserNameEditTextTextPersonName.setError(getResources().getString(R.string.user_name_to_small));
            return false;
        }
        if(newUserNameEditTextTextPersonName.getText().toString().length() == 0){
            newUserNameEditTextTextPersonName.setError(getResources().getString(R.string.username_empty));
            return false;
        }
        if(newUserNameEditTextTextPersonName.getText().toString().equals(userProfileNameEditText.getText().toString())){
            newUserNameEditTextTextPersonName.setError(getResources().getString(R.string.user_already_exist));
            return false;
        }
        return true;
    }

    private boolean isPasswordValid(EditText editText) {
        if(editText.getText().toString().length() < 8 || editText.getText().toString().length() > 20){
            editText.setError(getResources().getString(R.string.user_password_to_small));
            return false;
        }
        if(editText.getText().toString().length() == 0){
            editText.setError(getResources().getString(R.string.user_password_empty));
            return false;
        }
        return true;
    }

    private boolean isPasswordsEqual(){
        if(currentPasswordEditTextPassword.getText().toString().equals(newPasswordEditTextPassword.getText().toString())){
            newPasswordEditTextPassword.setError(getResources().getString(R.string.same_password));
            return false;
        }
        return true;
    }
    private void sentNewPassword() {
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi api = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        if(isPasswordValid(currentPasswordEditTextPassword) && isPasswordValid(newPasswordEditTextPassword)
        && isPasswordsEqual()) {
            api.changePassword("Bearer " + UserInfoService.getProperty("access_token"),
                            new ChangeUserDataRequest(currentPasswordEditTextPassword.getText().toString(),
                            newPasswordEditTextPassword.getText().toString()))
                    .enqueue(new Callback<MyMessage>() {
                        @Override
                        public void onResponse(@NotNull Call<MyMessage> call,
                                               @NotNull Response<MyMessage> response) {
                            if(response.body() != null){
                                MyMessage myMessage = response.body();
                                if(myMessage.getContent().equals(getResources()
                                        .getString(R.string.old_password_invalid))){
                                    currentPasswordEditTextPassword.setError(getResources()
                                            .getString(R.string.old_password_invalid));
                                }
                                else if(myMessage.getContent().equals(getResources()
                                        .getString(R.string.token_expired))){
                                    refreshToken();
                                }
                                else if(myMessage.getContent().equals(getResources()
                                        .getString(R.string.password_changed))){
                                    Toast.makeText(UserProfileActivity.this, getResources()
                                            .getString(R.string.password_changed),
                                            Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                                else{
                                    goLogin();
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<MyMessage> call, @NotNull Throwable t) {
                            Toast.makeText(UserProfileActivity.this,
                                    "Failed to change password",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void refreshToken() {
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi api = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        api.refreshToken("Bearer " + UserInfoService.getProperty("refresh_token"))
                .enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                        if(response.body() != null){
                            RegisterResponse registerResponse = response.body();
                            if(registerResponse.getAccessToken().equals(getResources()
                                    .getString(R.string.token_expired))){
                                goLogin();
                            }
                            else if(registerResponse.getAccessToken().equals(getResources()
                                    .getString(R.string.username_not_found))){
                                goLogin();
                            }
                            else {
                                UserInfoService.addProperty("access_token", registerResponse.getAccessToken());
                                UserInfoService.addProperty("refresh_token", registerResponse.getRefreshToken());
                                finish();
                                startActivity(getIntent());
                                if(dialog != null) {
                                    dialog.cancel();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        UserProfileActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(UserProfileActivity.this, "Failed to check user authentication",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    private void goLogin() {
        Intent intent = new Intent(this, UserLoginActivity.class);
        startActivity(intent);
    }

    private void goMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}