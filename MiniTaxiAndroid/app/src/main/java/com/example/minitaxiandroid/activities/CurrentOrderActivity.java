package com.example.minitaxiandroid.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.example.minitaxiandroid.R;
import com.example.minitaxiandroid.entities.document.DRIVER_STATUS;
import com.example.minitaxiandroid.entities.messages.UserSendDate;
import com.example.minitaxiandroid.services.DriverInfoService;
import com.example.minitaxiandroid.services.DriverLocationService;
import com.example.minitaxiandroid.websocket.WebSocketClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.concurrent.ListenableFuture;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;


public class CurrentOrderActivity extends AppCompatActivity {

    private TextView customerName;
    private TextView customerAddress;
    private TextView deliveryAddress;
    private TextView telephoneNumber;
    private Button completeButton;
    private UserSendDate userSendDate;
    private StompSession stompSession;
    private LocationManager locationManager;
    private DatabaseReference databaseReference;
    private DriverLocationService driverLocationService;
    private boolean allow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order);
        customerName = findViewById(R.id.customerNameOrderInfoText);
        customerAddress = findViewById(R.id.customerAddressOrderInfoText);
        deliveryAddress = findViewById(R.id.deliveryAddressOrderInfoText);
        telephoneNumber = findViewById(R.id.telephoneNumberOrderInfoText);
        completeButton = findViewById(R.id.completeOrderButton);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(
                "https://energy-taxi-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        driverLocationService = new DriverLocationService(Integer.parseInt(DriverInfoService.getProperty("driverId")),
                DRIVER_STATUS.IN_ORDER, databaseReference);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 15,
                driverLocationService);
        setInfo(savedInstanceState);
    }

    public void setInfo(Bundle savedInstanceState){
      userSendDate = new UserSendDate(
                Integer.parseInt(getDate(savedInstanceState, "driverId")),
                Integer.parseInt(getDate(savedInstanceState, "userId")),
                getDate(savedInstanceState, "customerName"),
                getDate(savedInstanceState, "customerAddress"),
                getDate(savedInstanceState, "deliveryAddress"),
                getDate(savedInstanceState, "telephoneNumber")
                );
        customerName.setText(userSendDate.getCustomerName());
        customerAddress.setText(userSendDate.getAddressCustomer());
        deliveryAddress.setText(userSendDate.getAddressDelivery());
        telephoneNumber.setText(userSendDate.getTelephoneNumber());
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            completeButton.setOnClickListener(view -> {
                completeOrder();
            });
        }, 10000);

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

    public void completeOrder(){
        new Thread(() -> {
            try {
                Log.d("CurrentOrder", "Run");
                WebSocketClient userClient = new WebSocketClient();
                ListenableFuture<StompSession> f = userClient.connect();
                stompSession = f.get();
                sendUserDateMessage();
                stompSession.disconnect();
                Intent intent = new Intent(CurrentOrderActivity.this, DriverMenuActivity.class);
                intent.putExtra("isOnline", "true");
                startActivity(intent);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void sendUserDateMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("{ \"driverId\" : ").append(userSendDate.getDriverId()).append(",")
                .append("\"userId\" : ").append(userSendDate.getUserId()).append(",")
                .append("\"customerName\" : \"").append(userSendDate.getCustomerName()).append("\",")
                .append("\"addressCustomer\" : \"").append(userSendDate.getAddressCustomer()).append("\",")
                .append("\"addressDelivery\" : \"").append(userSendDate.getAddressDelivery()).append("\",")
                .append("\"telephoneNumber\" : \"").append(userSendDate.getTelephoneNumber()).append("\" }");
        stompSession.send("/app/order-info-message", stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void onBackPressed() {
        if(allow) {
            super.onBackPressed();
        }
    }
}