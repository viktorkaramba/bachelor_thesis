package com.example.minitaxiandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.minitaxiandroid.R;
import com.example.minitaxiandroid.entities.messages.UserSendDate;
import com.example.minitaxiandroid.websocket.WebSocketClient;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.concurrent.ListenableFuture;

import java.lang.reflect.Type;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order);
        customerName = findViewById(R.id.customerNameOrderInfoText);
        customerAddress = findViewById(R.id.customerAddressOrderInfoText);
        deliveryAddress = findViewById(R.id.deliveryAddressOrderInfoText);
        telephoneNumber = findViewById(R.id.telephoneNumberOrderInfoText);
        completeButton = findViewById(R.id.completeOrderButton);
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
        completeButton.setOnClickListener(view -> {
            completeOrder();
        });
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
                subscribe(stompSession, userSendDate.getUserId());
                sendUserDateMessage(stompSession);
                Intent intent = new Intent(CurrentOrderActivity.this, DriverMenuActivity.class);
                System.out.println("CurrentOrder userId " + userSendDate.getDriverId());
                intent.putExtra("userId", String.valueOf(userSendDate.getDriverId()));
                startActivity(intent);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }

    public void subscribe(StompSession stompSession, int userID) throws ExecutionException, InterruptedException {
        stompSession.subscribe("/user/" + userID + "/order-message", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return byte[].class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object o) {
                Log.d("Received ", new String((byte[]) o));
            }
        });
    }

    public void sendUserDateMessage(StompSession stompSession) {
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

}