package com.example.minitaxiandroid.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.minitaxiandroid.R;
import com.example.minitaxiandroid.api.MiniTaxiApi;
import com.example.minitaxiandroid.api.RetrofitService;
import com.example.minitaxiandroid.entities.auth.RegisterResponse;
import com.example.minitaxiandroid.entities.messages.ResponseMessage;
import com.example.minitaxiandroid.services.DriverInfoService;
import com.example.minitaxiandroid.services.UserInfoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WelcomeActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button yesGpsPermissionButton, continueGpsPermissionButton, loginAsUserWelcomeButton,
            loginAsDriverWelcomeButton;
    private LocationManager locationManager;
    private String userAccessToken, userRefreshToken, driverAccessToken, driverRefreshToken;
    private boolean isSomethingDisable, isDriver = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        UserInfoService.init(WelcomeActivity.this);
        DriverInfoService.init(WelcomeActivity.this);
        userAccessToken = UserInfoService.getProperty("access_token");
        userRefreshToken = UserInfoService.getProperty("refresh_token");
        driverAccessToken = DriverInfoService.getProperty("access_token");
        driverRefreshToken = DriverInfoService.getProperty("refresh_token");
        System.out.println(userAccessToken);
        System.out.println(userRefreshToken);
        System.out.println(driverAccessToken);
        System.out.println(driverRefreshToken);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(WelcomeActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(WelcomeActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else{
            checkGPS();
        }
    }


    private void checkGPS() {
        if (!isGPSEnable()) {
            isSomethingDisable = true;
            showGPSPDisableDialog();
            Log.d("GPS", "GPS disable");
        } else {
            goNext();
        }
    }

    private void goNext(){
        if(userAccessToken == null && userRefreshToken == null
                && driverAccessToken == null && driverRefreshToken == null){
            goUserLoginActivity();
        }
        if(userAccessToken == null && userRefreshToken == null
                && driverAccessToken != null && driverRefreshToken != null){
            isDriver = true;
            userAuthentication(driverAccessToken);
        }
        if(userAccessToken != null && userRefreshToken != null
                && driverAccessToken == null && driverRefreshToken == null){
            userAuthentication(userAccessToken);
        }
        if(userAccessToken != null && userRefreshToken != null
                && driverAccessToken != null && driverRefreshToken != null){
            loginDriverOrUser();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isSomethingDisable = true;
                checkGPS();
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void showGPSPDisableDialog() {
        dialogBuilder = new AlertDialog.Builder(WelcomeActivity.this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.gps_permission_popwindow, null);
        initializeGPSDisableDialog(contactPopupView);
        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.card_view_border_reverse, getTheme()));
        dialog.show();
    }

    private void loginDriverOrUser() {
        dialogBuilder =new AlertDialog.Builder(WelcomeActivity.this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.login_user_or_driver_popwindow, null);
        initializeLoginDriverOrUserDialog(contactPopupView);
        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.card_view_border_reverse, getTheme()));
        dialog.show();
    }

    private void initializeLoginDriverOrUserDialog(View contactPopupView) {
        loginAsUserWelcomeButton = contactPopupView.findViewById(R.id.loginAsUserWelcomeButton);
        loginAsUserWelcomeButton.setOnClickListener(view -> {
            dialog.cancel();
            isDriver = false;
            userAuthentication(userAccessToken);
        });
        loginAsDriverWelcomeButton = contactPopupView.findViewById(R.id.loginAsDriverWelcomeButton);
        loginAsDriverWelcomeButton.setOnClickListener(view -> {
            dialog.cancel();
            isDriver = true;
            userAuthentication(driverAccessToken);
        });
    }

    private void initializeGPSDisableDialog(View contactPopupView) {
        yesGpsPermissionButton = contactPopupView.findViewById(R.id.yesGpsPermissionButton);
        yesGpsPermissionButton.setOnClickListener(view -> turnGPSOn());
        continueGpsPermissionButton = contactPopupView.findViewById(R.id.continueGpsPermissionButton);
        continueGpsPermissionButton.setOnClickListener(view -> goNext());
    }

    private void turnGPSOn(){
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }
    public void userAuthentication(String accessToken){
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi api = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        api.checkUserToken("Bearer " + accessToken)
                .enqueue(new Callback<ResponseMessage>() {
                    @Override
                    public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                        if(response.body() != null){
                            ResponseMessage responseMessage = response.body();
                            Log.d("Check Token Msg", responseMessage.getContent());
                            if(responseMessage.getContent().equals(getResources().getString(R.string.token_expired))){
                                if(!isDriver){
                                    System.out.println("In");
                                    getNewAccessToken(UserInfoService.getProperty("refresh_token"));
                                } else {
                                    getNewAccessToken(DriverInfoService.getProperty("refresh_token"));
                                }
                            }
                            else if(responseMessage.getContent().equals(getResources().getString(R.string.valid_token))){
                                if(!isDriver){
                                    UserInfoService.addProperty("rankId", responseMessage.getUserId());
                                    goMain();
                                } else {
                                    goDriverMenu();
                                }
                            }
                            else {
                                goUserLoginActivity();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseMessage> call, Throwable t) {
                        WelcomeActivity.this.runOnUiThread(() -> Toast.makeText(WelcomeActivity.this, "Failed to check user authentication",
                                Toast.LENGTH_SHORT).show());

                    }
                });
    }

    private void getNewAccessToken(String refreshToken) {
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi api = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        api.refreshToken("Bearer " + refreshToken)
                .enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                        if(response.body() != null){
                            System.out.println(response.body());
                            RegisterResponse registerResponse = response.body();
                            if(registerResponse.getAccessToken().equals(getResources().getString(R.string.token_expired))){
                                goUserLoginActivity();
                            }
                            else if(registerResponse.getAccessToken().equals(getResources().getString(R.string.username_not_found))){
                                goUserLoginActivity();
                            }
                            else {
                                if(!isDriver){
                                    UserInfoService.addProperty("access_token", registerResponse.getAccessToken());
                                    UserInfoService.addProperty("refresh_token", registerResponse.getRefreshToken());
                                    goMain();
                                } else {
                                    DriverInfoService.addProperty("access_token", registerResponse.getAccessToken());
                                    DriverInfoService.addProperty("refresh_token", registerResponse.getRefreshToken());
                                    goDriverMenu();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        WelcomeActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(WelcomeActivity.this, "Failed to check user authentication",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    public void goUserLoginActivity(){
        if(isSomethingDisable){
            Intent intent = new Intent(WelcomeActivity.this, UserLoginActivity.class);
            startActivity(intent);
        }
       else{
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(WelcomeActivity.this, UserLoginActivity.class);
                startActivity(intent);
            }, 3000);
       }
    }

    private void goMain() {
        if(isSomethingDisable){
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else{
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
            }, 2000);
        }
    }

    private void goDriverMenu() {
        if(isSomethingDisable){
            Intent intent = new Intent(WelcomeActivity.this, DriverMenuActivity.class);
            startActivity(intent);
        }
        else{
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(WelcomeActivity.this, DriverMenuActivity.class);
                startActivity(intent);
            }, 2000);
        }
    }

    private boolean isGPSEnable(){
        return  locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}