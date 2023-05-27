package com.example.energytaxiandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.energytaxiandroid.R;
import com.example.energytaxiandroid.api.MiniTaxiApi;
import com.example.energytaxiandroid.api.RetrofitService;
import com.example.energytaxiandroid.entities.document.DriverResume;
import com.example.energytaxiandroid.entities.messages.MyMessage;
import com.example.energytaxiandroid.services.DriverInfoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DriverRegistrationActivity extends AppCompatActivity {
    private EditText driverUserNameEditText;
    private EditText driverPasswordEditText;
    private EditText driverFirstNameEditText;
    private EditText driverSurNameEditText;
    private EditText driverPatronymicEditText;
    private EditText driverTelephoneNumberEditText;
    private EditText driverExperienceEditText;
    private Button sendButton;
    private Button cancelButton;
    private DriverResume driverResume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_registration);
        DriverInfoService.init(DriverRegistrationActivity.this);
        initializeComponents();
    }

    private void initializeComponents(){
        driverUserNameEditText = findViewById(R.id.driverUserNameEditText);
        driverPasswordEditText = findViewById(R.id.driverPasswordDriverEditText);
        driverFirstNameEditText = findViewById(R.id.driverFirstName);
        driverSurNameEditText = findViewById(R.id.driverSurName);
        driverPatronymicEditText= findViewById(R.id.driverPatronymic);
        driverTelephoneNumberEditText = findViewById(R.id.driverTelephoneNumber);
        driverExperienceEditText = findViewById(R.id.driverExperience);
        sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(view -> sendResume());
        cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(view -> goMain());
    }

    private void goMain() {
        Intent intent = new Intent(this, UserLoginActivity.class);
        startActivity(intent);
    }

    public void sendResume(){
        String driverUserName = String.valueOf(this.driverUserNameEditText.getText());
        String driverPassword = String.valueOf(this.driverPasswordEditText.getText());
        String driverFirstName = String.valueOf(this.driverFirstNameEditText.getText());
        String driverSurName = String.valueOf(this.driverSurNameEditText.getText());
        String driverPatronymic = String.valueOf(this.driverPatronymicEditText.getText());
        String driverTelephone = String.valueOf(this.driverTelephoneNumberEditText.getText());
        float driverExperience = Float.parseFloat(String.valueOf(this.driverExperienceEditText.getText()));
        driverResume = new DriverResume(driverUserName, driverPassword, driverFirstName, driverSurName,
                driverPatronymic, driverTelephone, driverExperience);
        DriverInfoService.addProperty("username", driverUserName);
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi userRegisterApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        if(isValidInfo()){
            userRegisterApi.driverRegister(driverResume)
                    .enqueue(new Callback<MyMessage>() {
                        @Override
                        public void onResponse(Call<MyMessage> call, Response<MyMessage> response) {
                            if( response.body() != null){
                                MyMessage myMessage = response.body();
                                Log.d("Diver Registration Received(/user/driver-registration)", myMessage.getContent());
                                getResponse(myMessage);
                            }
                        }

                        @Override
                        public void onFailure(Call<MyMessage> call, Throwable t) {
                            DriverRegistrationActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(DriverRegistrationActivity.this, "Failed to register. Try again later",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
        }
    }

    private void getResponse(MyMessage myMessage) {
        if(myMessage.getContent().equals("Resume added")){
            Toast.makeText(DriverRegistrationActivity.this,
                    "Your resume successfully added. Please wait answer",
                    Toast.LENGTH_SHORT).show();
            goMain();
        }
        else{
            Toast.makeText(DriverRegistrationActivity.this,
                    "Error to add resume. Please try again later",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidInfo(){
        if(driverUserNameEditText.getText().toString().length() < 4
                || driverUserNameEditText.getText().toString().length() > 16){
            driverUserNameEditText.setError(getResources().getString(R.string.user_name_to_small));
            return false;
        }
        if(driverUserNameEditText.getText().toString().length() == 0){
            driverUserNameEditText.setError(getResources().getString(R.string.username_empty));
            return false;
        }
        if(driverPasswordEditText.getText().toString().length() < 8
                || driverPasswordEditText.getText().toString().length() > 20){
            driverPasswordEditText.setError(getResources().getString(R.string.user_password_to_small));
            return false;
        }
        if(driverPasswordEditText.getText().toString().length() == 0){
            driverPasswordEditText.setError(getResources().getString(R.string.user_password_empty));
            return false;
        }
        if(driverFirstNameEditText.getText().toString().length() < 2
                || driverFirstNameEditText.getText().toString().length() > 20){
            driverFirstNameEditText.setError(getResources().getString(R.string.driver_first_name_to_small));
            return false;
        }
        if(driverFirstNameEditText.getText().toString().length() == 0){
            driverFirstNameEditText.setError(getResources().getString(R.string.driver_first_name_empty));
            return false;
        }
        if(driverSurNameEditText.getText().toString().length() < 2
                || driverSurNameEditText.getText().toString().length() > 30){
            driverSurNameEditText.setError(getResources().getString(R.string.driver_surname_to_small));
            return false;
        }
        if(driverSurNameEditText.getText().toString().length() == 0){
            driverSurNameEditText.setError(getResources().getString(R.string.driver_surname_empty));
            return false;
        }
        if(driverPatronymicEditText.getText().toString().length() < 2
                || driverPatronymicEditText.getText().toString().length() > 30){
            driverPatronymicEditText.setError(getResources().getString(R.string.driver_patronymic_to_small));
            return false;
        }
        if(driverPatronymicEditText.getText().toString().length() == 0){
            driverPatronymicEditText.setError(getResources().getString(R.string.driver_patronymic_empty));
            return false;
        }
        if(Float.parseFloat(driverExperienceEditText.getText().toString()) < 0) {
            driverExperienceEditText.setError(getResources().getString(R.string.driver_experience_negative));
            return false;
        }
        if(driverExperienceEditText.getText().toString().length() == 0){
            driverExperienceEditText.setError(getResources().getString(R.string.driver_experience_empty));
            return false;
        }
        Pattern pattern = Pattern.compile("[+]380\\d{9}");
        Matcher matcher = pattern.matcher(driverTelephoneNumberEditText.getText().toString());
        if(!matcher.matches()){
            driverTelephoneNumberEditText.setError(getResources().getString(R.string.driver_telephone_invalid));
            return false;
        }
        return true;
    }
}