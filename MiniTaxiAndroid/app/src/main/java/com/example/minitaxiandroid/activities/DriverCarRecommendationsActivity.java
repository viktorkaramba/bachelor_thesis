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
import com.example.minitaxiandroid.entities.messages.CarRecommendationInfo;
import com.example.minitaxiandroid.fragments.CarRecFragment;
import com.example.minitaxiandroid.fragments.EmptyCarRecFragment;
import com.example.minitaxiandroid.api.MiniTaxiApi;
import com.example.minitaxiandroid.api.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

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
        back.setOnClickListener(view -> gotoDriveMenu());
        getCarRecInfo();
    }

    private void gotoDriveMenu() {
        Intent intent = new Intent(this, DriverMenuActivity.class);
        intent.putExtra("driverId", driverId);
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
        System.out.println("current " + driverId);
        carRecInfo.getDriverCarRecommendations(driverId)
                .enqueue(new Callback<CarRecommendationInfo>() {
                    @Override
                    public void onResponse(@EverythingIsNonNull Call<CarRecommendationInfo> call,
                                           @EverythingIsNonNull Response<CarRecommendationInfo> response) {
                        System.out.println(response.body());
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        if(response.body() != null){
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
                        Toast.makeText(DriverCarRecommendationsActivity.this,
                                "Failed to load car recommendation info",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

}