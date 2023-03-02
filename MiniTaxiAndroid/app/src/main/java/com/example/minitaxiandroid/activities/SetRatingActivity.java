package com.example.minitaxiandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.annotation.Nullable;
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

import static com.example.minitaxiandroid.services.ObjectParserService.parseUserSendDateFromString;


public class SetRatingActivity extends AppCompatActivity {

    private RatingBar ratingDriverBar;
    private Button evaluate;
    private UserSendDate userSendDate;
    private StompSession stompSession;
    private String userId;
    private String price;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_rating);
        ratingDriverBar = findViewById(R.id.ratingDriverBar);
        evaluate = findViewById(R.id.evaluateRatingButton);
        ratingDriverBar.setEnabled(false);
        ratingDriverBar.setRating(2.5f);
        evaluate.setEnabled(false);
        evaluate.setOnClickListener(view -> {
            new Thread(this::sendOrder).start();
            goListDriver();
        });
        new Thread(() -> {
            WebSocketClient userClient = new WebSocketClient();
            ListenableFuture<StompSession> f = userClient.connect();
            userId = getDate(savedInstanceState, "userId");
            price = getDate(savedInstanceState, "price");
            try {
                stompSession = f.get();
                subscribe(stompSession, Integer.parseInt(userId));
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
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

    public void subscribe(StompSession stompSession, int userID) throws ExecutionException, InterruptedException {
        stompSession.subscribe("/user/" + userID + "/order-message", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return byte[].class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object o) {
                String response = new String((byte[]) o);
                Log.d("Set Rating Received ", response);
                setEnabledButtons();
                try {
                    userSendDate = parseUserSendDateFromString(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(SetRatingActivity.this, "Please evaluate work of driver",
                        Toast.LENGTH_SHORT).show();
            }
        });
        stompSession.subscribe("/user/" + userID + "/order-complete", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return byte[].class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object o) {
                String response = new String((byte[]) o);
                Log.d("Received ", response);
                Toast.makeText(SetRatingActivity.this, "Please evaluate work of driver",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setEnabledButtons() {
        runOnUiThread(() -> {
            evaluate.setEnabled(true);
            ratingDriverBar.setEnabled(true);
        });
    }

    private void goListDriver() {
        Intent intent = new Intent(SetRatingActivity.this, UserOrderHistoryActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    private void sendOrder() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("{ \"driverId\" : ").append(userSendDate.getDriverId()).append(",")
                .append("\"userId\" : ").append(userId).append(",")
                .append("\"customerName\" : \"").append(userSendDate.getCustomerName()).append("\",")
                .append("\"addressCustomer\" : \"").append(userSendDate.getAddressCustomer()).append("\",")
                .append("\"addressDelivery\" : \"").append(userSendDate.getAddressDelivery()).append("\",")
                .append("\"telephoneNumber\" : \"").append(userSendDate.getTelephoneNumber()).append("\",")
                .append("\"price\" : \"").append(price).append("\",")
                .append("\"rating\" : \"").append(ratingDriverBar.getRating()).append("\" }");
        System.out.println(stringBuilder);
        stompSession.send("/app/order-complete-message", stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
    }

}
