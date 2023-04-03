package com.example.minitaxiandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.minitaxiandroid.R;
import com.example.minitaxiandroid.entities.messages.PriceByClass;
import com.example.minitaxiandroid.entities.messages.ResponseMessage;
import com.example.minitaxiandroid.entities.messages.UserSendDate;
import com.example.minitaxiandroid.services.DriverInfoService;
import com.example.minitaxiandroid.services.UserLoginInfoService;
import com.example.minitaxiandroid.websocket.WebSocketClient;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.concurrent.ListenableFuture;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

import static com.example.minitaxiandroid.services.ObjectParserService.parsePriceByClassFromString;
import static com.example.minitaxiandroid.services.ObjectParserService.parseResponseMessageFromString;

;

public class MakeOrderActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private Button makeOrderButton;
    private Button cancelMakeOrderButton;
    private TextView priceStandardTextView;
    private TextView priceComfortTextView;
    private TextView priceEliteTextView;
    private RadioButton standardRadioButton;
    private RadioButton comfortRadioButton;
    private RadioButton eliteRadioButton;
    private DriverResponseDialog dialog;
    private String userId;
    private String userAddressFrom;
    private String userAddressTo;
    private StompSession stompSession;
    private DriverInfoService driverInfoService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);
        makeOrderButton = findViewById(R.id.makeOrderButton);
        cancelMakeOrderButton = findViewById(R.id.cancelMakeOrderButton);
        priceStandardTextView = findViewById(R.id.priceStandardTextView);
        priceComfortTextView = findViewById(R.id.priceComfortTextView);
        priceEliteTextView = findViewById(R.id.priceEliteTextView);
        standardRadioButton = findViewById(R.id.standardRadioButton);
        standardRadioButton.setOnCheckedChangeListener(this);
        comfortRadioButton = findViewById(R.id.comfortRadioButton);
        comfortRadioButton.setOnCheckedChangeListener(this);
        eliteRadioButton = findViewById(R.id.eliteRadioButton);
        eliteRadioButton.setOnCheckedChangeListener(this);
        userAddressFrom = getDate(savedInstanceState, "userAddressFrom");
        userAddressTo = getDate(savedInstanceState, "userAddressTo");
        getPriceByClass();
        userId = UserLoginInfoService.getProperty("userId");
        dialog = new DriverResponseDialog();
        makeOrderButton.setOnClickListener(view -> {
            try {
                makeOrder();
//                showWaitForDriverResponse();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        cancelMakeOrderButton.setOnClickListener(view -> goMain());
    }

    public void getPriceByClass(){
        new Thread(() -> {
            try {
                Log.d("GetPriceByClass", "Run");
                WebSocketClient userClient = new WebSocketClient();
                ListenableFuture<StompSession> f = userClient.connect();
                stompSession = f.get();
                subscribeUser(Integer.parseInt(userId));
                sendUserAddresses();
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

    private void sendUserAddresses(){
        String message = "{ \"userId\" : " + userId + "," +
                "\"userAddressFrom\" : \"" + userAddressFrom + "\"," +
                "\"userAddressTo\" : \"" + userAddressTo + "\" }";
        stompSession.send("/app/user-addresses-message", message.getBytes(StandardCharsets.UTF_8));
    }

    private void showWaitForDriverResponse() {
        dialog.setLayout(R.layout.user_wait_response);
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(),"wait response");
    }


    private void makeOrder() throws ExecutionException, InterruptedException {
        driverInfoService = new DriverInfoService();
        driverInfoService.disconnect();
//       new Thread(() -> {
//           try {
//               Log.d("MakeOrder", "Run");
//               WebSocketClient userClient = new WebSocketClient();
//               ListenableFuture<StompSession> f = userClient.connect();
//               stompSession = f.get();
////               subscribeUser(stompSession, Integer.parseInt(userId));
////               UserSendDate userSendDate = new UserSendDate(Integer.parseInt(driverId),
////                       Integer.parseInt(userId), customerName.getText().toString(),
////                       customerAddress.getText().toString(), deliveryAddress.getText().toString(),
////                       telephoneNumberCustomer.getText().toString());
////               sendUserDateMessage(stompSession, userSendDate);
//           } catch (ExecutionException | InterruptedException e) {
//               e.printStackTrace();
//           }
//       }).start();
    }

    public void subscribeUser(int userID) throws ExecutionException, InterruptedException {
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
        stompSession.subscribe("/user/" + userID + "/prices-by-class", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return byte[].class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object o) {
                String response = new String((byte[]) o);
                try {
                    setTextPrice(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    private void setTextPrice(String response) throws Exception {
        runOnUiThread(() -> {
            PriceByClass priceByClasses = null;
            try {
                priceByClasses = parsePriceByClassFromString(response);
                System.out.println(priceByClasses.getPriceByClass().get(0));
                String str1 = "PRICE: " + priceByClasses.getPriceByClass().get(0);
                String str2 = "PRICE: " + priceByClasses.getPriceByClass().get(1);
                String str3 = "PRICE: " + priceByClasses.getPriceByClass().get(2);
                priceStandardTextView.setText(str1);
                priceComfortTextView.setText(str2);
                priceEliteTextView.setText(str3);
            } catch (Exception e) {
                e.printStackTrace();
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
//        intent.putExtra("userId", userId);
//        intent.putExtra("userId", userId);
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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            if (compoundButton.getId() == R.id.standardRadioButton) {
                comfortRadioButton.setChecked(false);
                eliteRadioButton.setChecked(false);
            }
            if (compoundButton.getId() == R.id.comfortRadioButton) {
                standardRadioButton.setChecked(false);
                eliteRadioButton.setChecked(false);
            }
            if (compoundButton.getId() == R.id.eliteRadioButton) {
                standardRadioButton.setChecked(false);
                comfortRadioButton.setChecked(false);
            }
        }
    }
}
