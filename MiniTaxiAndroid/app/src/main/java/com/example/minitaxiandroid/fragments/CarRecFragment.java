package com.example.minitaxiandroid.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.minitaxiandroid.R;
import com.example.minitaxiandroid.activities.UserLoginActivity;
import com.example.minitaxiandroid.api.MiniTaxiApi;
import com.example.minitaxiandroid.api.RetrofitService;
import com.example.minitaxiandroid.entities.auth.RegisterResponse;
import com.example.minitaxiandroid.entities.document.CAR_CLASSES;
import com.example.minitaxiandroid.entities.document.DriverRecAnswer;
import com.example.minitaxiandroid.entities.messages.CarRecommendationInfo;
import com.example.minitaxiandroid.entities.messages.MyMessage;
import com.example.minitaxiandroid.services.DriverInfoService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

public class CarRecFragment extends Fragment {

    private TextView car;
    private TextView carClass;
    private TextView pricePerKilometer;
    private TextView salary;
    private Button accept, reject;
    private CarRecommendationInfo carRecommendationInfo;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_car_rec, container, false);
        car = view.findViewById(R.id.carDriveCarRecEditText);
        carClass = view.findViewById(R.id.carClassDriveCarRecEditText);
        pricePerKilometer = view.findViewById(R.id.pricePKDriverCarRecEditText);
        salary = view.findViewById(R.id.salaryDriveCarRecEditText);
        accept = view.findViewById(R.id.acceptDriveCarRecButton);
        reject = view.findViewById(R.id.rejectDriveCarRecButton);
        accept.setOnClickListener(view12 -> accept());
        reject.setOnClickListener(view1 -> reject());
        DriverInfoService.init(CarRecFragment.this.getContext());
        carRecommendationInfo = new CarRecommendationInfo();
        carRecommendationInfo.setDriverCarRecId(Integer.parseInt(getArguments().getString("driverCarRecId")));
        carRecommendationInfo.setDriverId(Integer.parseInt(getArguments().getString("driverId")));
        carRecommendationInfo.setCarId(Integer.parseInt(getArguments().getString("carId")));
        carRecommendationInfo.setCarProducer(getArguments().getString("carProducer"));
        carRecommendationInfo.setCarBrand( getArguments().getString("carBrand"));
        carRecommendationInfo.setPricePerKilometer(Float.parseFloat(getArguments().getString("pricePerKilometer")));
        carRecommendationInfo.setCarClass(CAR_CLASSES.valueOf(getArguments().getString("carClass")));
        carRecommendationInfo.setNewSalary(Float.parseFloat(getArguments().getString("newSalary")));
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(
                "https://energy-taxi-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();
        setDate();
        return view;
    }

    private void accept() {
        setAnswer("Yes");
        changeInFireBase();
    }

    private void changeInFireBase() {
        String id = "driver-"+ DriverInfoService.getProperty("driverId");
        databaseReference.child("drivers-info").child(id).child("carClass").
                setValue(carRecommendationInfo.getCarClass().name()).addOnCompleteListener(task -> {
                    if(task.isComplete()){
                        Log.d("TAG", "Driver status successfully changed");
                        Toast.makeText(CarRecFragment.this.getContext(), "Driver status successfully changed",
                                Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Log.d("TAG", "Error to change driver status");
                        Toast.makeText(CarRecFragment.this.getContext(),
                                "Error to change driver status", Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReference.child("drivers-info").child(id).child("carBrand").
                setValue(carRecommendationInfo.getCarBrand()).addOnCompleteListener(task -> {
                    if(task.isComplete()){
                        Log.d("TAG", "Driver car brand successfully changed");
                        Toast.makeText(CarRecFragment.this.getContext(), "Driver car brand successfully changed",
                                Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Log.d("TAG", "Error to change driver status");
                        Toast.makeText(CarRecFragment.this.getContext(),
                                "Error to change driver car brand", Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReference.child("drivers-info").child(id).child("carProducer").
                setValue(carRecommendationInfo.getCarProducer()).addOnCompleteListener(task -> {
                    if(task.isComplete()){
                        Log.d("TAG", "Driver car producer successfully changed");
                        Toast.makeText(CarRecFragment.this.getContext(), "Driver car producer successfully changed",
                                Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Log.d("TAG", "Error to change driver status");
                        Toast.makeText(CarRecFragment.this.getContext(),
                                "Error to change driver car producer", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void reject() {
        setAnswer("No");
    }

    private void setAnswer(String answer){
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi driverAnswer = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        DriverRecAnswer driverRecAnswer = new DriverRecAnswer(carRecommendationInfo.getDriverCarRecId(),
                carRecommendationInfo.getNewSalary(), carRecommendationInfo.getCarId(), carRecommendationInfo.getDriverId(), answer);
        driverAnswer.getAnswer("Bearer " + DriverInfoService.getProperty("access_token"),
                driverRecAnswer).enqueue(new Callback<MyMessage>() {
            @Override
            public void onResponse(Call<MyMessage> call, Response<MyMessage> response) {
                if(response.body()!=null){
                    try {
                        if(response.errorBody() != null){
                            if(response.errorBody().string().contains(getResources()
                                    .getString(R.string.token_expired))){
                                refreshToken();
                            }
                        }
                        else{
                            replaceFragment();
                            Log.d("response ok", response.body().getContent());
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            }

            @Override
            public void onFailure(Call<MyMessage> call, Throwable t) {
                CarRecFragment.this.getActivity().runOnUiThread(() ->
                        Toast.makeText(CarRecFragment.this.getContext(),
                        "Failed to set answer",
                        Toast.LENGTH_SHORT).show());
                Log.d("response fail", call.toString());
            }
        });
    }

    private void replaceFragment() {
        EmptyCarRecFragment emptyCarRecFragment = new EmptyCarRecFragment();
        this.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, emptyCarRecFragment).commit();
    }

    public void setDate(){
        String carText = carRecommendationInfo.getCarProducer() + " " + carRecommendationInfo.getCarBrand();
        car.setText(carText);
        carClass.setText(carRecommendationInfo.getCarClass().name());
        pricePerKilometer.setText(String.valueOf(carRecommendationInfo.getPricePerKilometer()));
        salary.setText(String.valueOf(carRecommendationInfo.getNewSalary()));
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
                                requireActivity().getSupportFragmentManager().popBackStack();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        CarRecFragment.this.getActivity().runOnUiThread(() ->
                                Toast.makeText(CarRecFragment.this.getContext(), "Failed to check user authentication",
                                Toast.LENGTH_SHORT).show());

                    }
                });
    }
    private void goLogin() {
        Intent intent = new Intent(this.getContext(), UserLoginActivity.class);
        startActivity(intent);
    }
}