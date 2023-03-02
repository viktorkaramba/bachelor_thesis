package com.example.minitaxiandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.minitaxiandroid.R;
import com.example.minitaxiandroid.entities.document.DriverProfileInfo;
import com.example.minitaxiandroid.retrofit.MiniTaxiApi;
import com.example.minitaxiandroid.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class DriverProfile extends AppCompatActivity {

    private TextView driverName;
    private TextView carName;
    private TextView driverExperience;
    private TextView driverSalary;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);
        driverName = findViewById(R.id.driverProfileNameEditText);
        carName = findViewById(R.id.driverProfileCarNameEditText);
        driverExperience = findViewById(R.id.driverProfileExperienceEditText);
        driverSalary = findViewById(R.id.driverProfileSalaryEditText);
        backButton = findViewById(R.id.driverProfileBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goDriverMenu(savedInstanceState);
            }
        });
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi driverProfileInfo = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        String id = getDate(savedInstanceState, "driverId");
        System.out.println("driver profile " + id);
        driverProfileInfo.getDriverInfo(id)
                .enqueue(new Callback<DriverProfileInfo>() {

                    @Override
                    public void onResponse(@EverythingIsNonNull Call<DriverProfileInfo> call,
                                           @EverythingIsNonNull Response<DriverProfileInfo> response) {
                        populateListView(response.body());
                    }

                    @Override
                    public void onFailure(@EverythingIsNonNull Call<DriverProfileInfo> call, Throwable t) {
                        Toast.makeText(DriverProfile.this, "Failed to load driver profile info",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void populateListView(DriverProfileInfo body) {
        System.out.println(body);
        String fullName = body.getDriverFirstName() + " " + body.getDriverSurName()
                + " " + body.getDriverPatronymic();
        String carNameText = body.getCarProducer() + " " + body.getCarBrand();
        String driverExperienceText = String.valueOf(body.getExperience());
        String driverSalaryText = String.valueOf(body.getSalary());
        driverName.setText(fullName);
        carName.setText(carNameText);
        driverExperience.setText(driverExperienceText);
        driverSalary.setText(driverSalaryText);
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

    public void goDriverMenu(Bundle savedInstanceState){
        Intent intent = new Intent(this, DriverMenuActivity.class);
        intent.putExtra("userId", getDate(savedInstanceState, "driverId"));
        startActivity(intent);
    }
}