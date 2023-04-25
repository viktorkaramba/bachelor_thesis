package com.example.minitaxiandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import com.example.minitaxiandroid.R;
import com.example.minitaxiandroid.api.MiniTaxiApi;
import com.example.minitaxiandroid.api.RetrofitService;
import com.example.minitaxiandroid.entities.auth.RegisterResponse;
import com.example.minitaxiandroid.entities.messages.CarRecommendationInfo;
import com.example.minitaxiandroid.fragments.CarRecFragment;
import com.example.minitaxiandroid.fragments.EmptyCarRecFragment;
import com.example.minitaxiandroid.services.DriverInfoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

import java.io.IOException;

public class DriverCarRecommendationsActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    private String driverId;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_car_recommendations);
        frameLayout = findViewById(R.id.frameLayout);
        driverId = getDate(savedInstanceState, "driverId");
        back = findViewById(R.id.backDriverCarRecButton);
        DriverInfoService.init(DriverCarRecommendationsActivity.this);
        back.setOnClickListener(view -> gotoDriveMenu(savedInstanceState));
        getCarRecInfo();
    }

    private void gotoDriveMenu(Bundle savedInstanceState) {
        Intent intent = new Intent(this, DriverMenuActivity.class);
        intent.putExtra("driverId", driverId);
        intent.putExtra("isOnline", getDate(savedInstanceState, "isOnline"));
        startActivity(intent);
    }

    public String getDate(Bundle savedInstanceState, String key){
        String result;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                result= null;
            } else {
                result= extras.getString(key);
            }
        } else {
            result= (String) savedInstanceState.getSerializable(key);
        }
        return result;
    }

    public void getCarRecInfo(){
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi carRecInfo = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        carRecInfo.getDriverCarRecommendations("Bearer " + DriverInfoService.getProperty("access_token"),
                        driverId)
                .enqueue(new Callback<CarRecommendationInfo>() {
                    @Override
                    public void onResponse(@EverythingIsNonNull Call<CarRecommendationInfo> call,
                                           @EverythingIsNonNull Response<CarRecommendationInfo> response) {
                        System.out.println(response.body());
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        if(response.body() != null){
                            try {
                                if(response.errorBody() != null){
                                    if(response.errorBody().string().contains(getResources()
                                            .getString(R.string.token_expired))){
                                        refreshToken();
                                    }
                                }
                                else{
                                    Bundle bundle = new Bundle();
                                    bundle.putString("driverCarRecId", String.valueOf(response.body().getDriverCarRecId()));
                                    bundle.putString("driverId", String.valueOf(response.body().getDriverId()));
                                    bundle.putString("carId", String.valueOf(response.body().getCarId()));
                                    bundle.putString("carProducer", String.valueOf(response.body().getCarProducer()));
                                    bundle.putString("carBrand", String.valueOf(response.body().getCarBrand()));
                                    bundle.putString("pricePerKilometer", String.valueOf(response.body().getPricePerKilometer()));
                                    bundle.putString("carClass", String.valueOf(response.body().getCarClass()));
                                    bundle.putString("newSalary", String.valueOf(response.body().getNewSalary()));
                                    CarRecFragment carRecFragment = new CarRecFragment();
                                    carRecFragment.setArguments(bundle);
                                    ft.replace(R.id.frameLayout, carRecFragment);
                                    ft.commit();
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        else {
                            System.out.println("In empty");
                            EmptyCarRecFragment emptyCarRecFragment = new EmptyCarRecFragment();
                            ft.replace(R.id.frameLayout, emptyCarRecFragment);
                            ft.commit();
                        }
                    }

                    @Override
                    public void onFailure(Call<CarRecommendationInfo> call, Throwable t) {
                        Log.d("Faile rec", "error car rec");
                        DriverCarRecommendationsActivity.this.runOnUiThread(() ->
                                Toast.makeText(DriverCarRecommendationsActivity.this,
                                "Failed to load car recommendation info",
                                Toast.LENGTH_SHORT).show());

                    }
                });
    }


    private void goLogin() {
        Intent intent = new Intent(this, UserLoginActivity.class);
        startActivity(intent);
    }
    private void refreshToken() {
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi api = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        api.refreshToken("Bearer " + DriverInfoService.getProperty("refresh_token"))
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
                                DriverInfoService.addProperty("access_token", registerResponse.getAccessToken());
                                DriverInfoService.addProperty("refresh_token", registerResponse.getRefreshToken());
                                finish();
                                startActivity(getIntent());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        DriverCarRecommendationsActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(DriverCarRecommendationsActivity.this, "Failed to check user authentication",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }
}