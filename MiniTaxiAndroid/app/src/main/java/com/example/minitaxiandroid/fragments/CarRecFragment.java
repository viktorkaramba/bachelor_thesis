package com.example.minitaxiandroid.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.minitaxiandroid.R;
import com.example.minitaxiandroid.entities.document.DriverRecAnswer;
import com.example.minitaxiandroid.entities.messages.CarRecommendationInfo;
import com.example.minitaxiandroid.entities.messages.Message;
import com.example.minitaxiandroid.api.MiniTaxiApi;
import com.example.minitaxiandroid.api.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarRecFragment extends Fragment {

    private TextView car;
    private TextView carClass;
    private TextView pricePerKilometer;
    private TextView salary;
    private Button accept, reject;
    private CarRecommendationInfo carRecommendationInfo;

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
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accept();
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reject();
            }
        });
        carRecommendationInfo = new CarRecommendationInfo();
        carRecommendationInfo.setDriverCarRecId(Integer.parseInt(getArguments().getString("driverCarRecId")));
        carRecommendationInfo.setDriverId(Integer.parseInt(getArguments().getString("driverId")));
        carRecommendationInfo.setCarId(Integer.parseInt(getArguments().getString("carId")));
        carRecommendationInfo.setCarProducer(getArguments().getString("carProducer"));
        carRecommendationInfo.setCarBrand( getArguments().getString("carBrand"));
        carRecommendationInfo.setPricePerKilometer(Float.parseFloat(getArguments().getString("pricePerKilometer")));
        carRecommendationInfo.setCarClass(getArguments().getString("carClass"));
        carRecommendationInfo.setNewSalary(Float.parseFloat(getArguments().getString("newSalary")));
        setDate();
        return view;
    }

    private void accept() {
        setAnswer("Yes");
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void reject() {
        setAnswer("No");
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void setAnswer(String answer){
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi driverAnswer = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        DriverRecAnswer driverRecAnswer = new DriverRecAnswer(carRecommendationInfo.getDriverCarRecId(),
                carRecommendationInfo.getNewSalary(), carRecommendationInfo.getCarId(), carRecommendationInfo.getDriverId(), answer);
        driverAnswer.getAnswer(driverRecAnswer).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                Log.d("response ok", response.body().getContent());
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.d("response fail", call.toString());
            }
        });
    }
    public void setDate(){
        String carText = carRecommendationInfo.getCarProducer() + " " + carRecommendationInfo.getCarBrand();
        car.setText(carText);
        carClass.setText(carRecommendationInfo.getCarClass());
        pricePerKilometer.setText(String.valueOf(carRecommendationInfo.getPricePerKilometer()));
        salary.setText(String.valueOf(carRecommendationInfo.getNewSalary()));
    }
}