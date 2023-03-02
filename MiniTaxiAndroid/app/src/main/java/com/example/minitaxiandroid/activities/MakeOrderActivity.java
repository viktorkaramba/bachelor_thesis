package com.example.minitaxiandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.minitaxiandroid.R;
import com.example.minitaxiandroid.entities.messages.ResponseMessage;
import com.example.minitaxiandroid.entities.messages.UserSendDate;
import com.example.minitaxiandroid.websocket.WebSocketClient;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.concurrent.ListenableFuture;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

import static com.example.minitaxiandroid.services.ObjectParserService.parseResponseMessageFromString;

;

public class MakeOrderActivity extends AppCompatActivity {
    private EditText customerName;
    private EditText customerAddress;
    private EditText deliveryAddress;
    private EditText telephoneNumberCustomer;
    private Button makeOrderButton;
    private Button cancelMakeOrderButton;
    private DriverResponseDialog dialog;
    private String userId;
    private String driverId;
    private String price;
    private StompSession stompSession;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);
        customerName = findViewById(R.id.customerName);
        customerAddress = findViewById(R.id.customerAddress);
        deliveryAddress = findViewById(R.id.deliveryAddress);
        telephoneNumberCustomer = findViewById(R.id.telephoneNumberCustomer);
        makeOrderButton = findViewById(R.id.makeOrderButton);
        cancelMakeOrderButton = findViewById(R.id.cancelMakeOrderButton);
        driverId = getDate(savedInstanceState, "driverId");
        userId = getDate(savedInstanceState, "userId");
        price = getDate(savedInstanceState, "price");
        dialog = new DriverResponseDialog();
        makeOrderButton.setOnClickListener(view -> {
            try {
                makeOrder();
                showWaitForDriverResponse();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        cancelMakeOrderButton.setOnClickListener(view -> goMain());
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

    private void showWaitForDriverResponse() {
        dialog.setLayout(R.layout.user_wait_response);
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(),"wait response");
    }


    private void makeOrder() throws ExecutionException, InterruptedException {
       new Thread(() -> {
           try {
               Log.d("MakeOrder", "Run");
               WebSocketClient userClient = new WebSocketClient();
               ListenableFuture<StompSession> f = userClient.connect();
               stompSession = f.get();
               subscribeUser(stompSession, Integer.parseInt(userId));
               UserSendDate userSendDate = new UserSendDate(Integer.parseInt(driverId),
                       Integer.parseInt(userId), customerName.getText().toString(),
                       customerAddress.getText().toString(), deliveryAddress.getText().toString(),
                       telephoneNumberCustomer.getText().toString());
               sendUserDateMessage(stompSession, userSendDate);
           } catch (ExecutionException | InterruptedException e) {
               e.printStackTrace();
           }
       }).start();
    }

    public void subscribeUser(StompSession stompSession, int userID) throws ExecutionException, InterruptedException {
        stompSession.subscribe("/user/" + userID + "/driver", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return byte[].class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object o) {
                Log.d("\"Make Order Received ", new String((byte[]) o));
            }
        });
        stompSession.subscribe("/user/" + userID + "/order-accept", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return byte[].class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object o) {
                String response = new String((byte[]) o);
                Log.d("Make Order Received ", response);
                try {
                    ResponseMessage responseMessage = parseResponseMessageFromString(response);
                    if(responseMessage.getContent().equals("Yes")){
                        showAcceptDriverResponse();
                    }
                    else {
                        showRejectDriverResponse();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showAcceptDriverResponse() {
        dialog.dismiss();
        dialog.setLayout(R.layout.user_accept_response);
        dialog.show(getSupportFragmentManager(), "wait response");
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
//            dialog.dismiss();
            finish();
        }, 5000);
        Intent intent = new Intent(MakeOrderActivity.this, SetRatingActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("price", price);
        startActivity(intent);
    }

    private void showRejectDriverResponse() {
        dialog.dismiss();
        dialog.setLayout(R.layout.user_reject_response);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            dialog.show(getSupportFragmentManager(), "wait response");
//            dialog.dismiss();
            finish();
        }, 5000);
        Intent intent = new Intent(MakeOrderActivity.this, UserOrderHistoryActivity.class);
        intent.putExtra("userId", userId);
    }
    public void sendUserDateMessage(StompSession stompSession, UserSendDate userSendDate) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("{ \"driverId\" : ").append(userSendDate.getDriverId()).append(",")
                .append("\"userId\" : \"").append(userSendDate.getUserId()).append("\",")
                .append("\"customerName\" : \"").append(userSendDate.getCustomerName()).append("\",")
                .append("\"addressCustomer\" : \"").append(userSendDate.getAddressCustomer()).append("\",")
                .append("\"addressDelivery\" : \"").append(userSendDate.getAddressDelivery()).append("\",")
                .append("\"telephoneNumber\" : \"").append(userSendDate.getTelephoneNumber()).append("\" }");
        stompSession.send("/app/driver-message", stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
    }

    public void goMain(){
        Intent intent = new Intent(this, UserLoginActivity.class);
        intent.putExtra("userId",userId);
        startActivity(intent);
    }
}
