package com.example.minitaxiandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.minitaxiandroid.R;
import com.example.minitaxiandroid.adapter.OrderInfoProfileAdapter;
import com.example.minitaxiandroid.entities.messages.OrderInfo;
import com.example.minitaxiandroid.entities.messages.ResponseMessage;
import com.example.minitaxiandroid.entities.messages.UserPickCar;
import com.example.minitaxiandroid.entities.messages.UserSendDate;
import com.example.minitaxiandroid.entities.userinfo.FavouriteAddressesUserInfo;
import com.example.minitaxiandroid.entities.userinfo.FavouriteDriverUserInfo;
import com.example.minitaxiandroid.entities.userinfo.UserOrderInfo;
import com.example.minitaxiandroid.retrofit.MiniTaxiApi;
import com.example.minitaxiandroid.retrofit.RetrofitService;
import com.example.minitaxiandroid.retrofit.SelectListener;
import com.example.minitaxiandroid.websocket.WebSocketClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.concurrent.ListenableFuture;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.minitaxiandroid.services.ObjectParserService.parseUserSendDateFromString;

public class DriverMenuActivity extends AppCompatActivity implements SelectListener {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView customerName, telephoneCustomer, addressCustomer, addressDelivery;
    private Button accept, cancel, driverCarRec;
    private FloatingActionButton profileButton;
    private StompSession stompSession;
    private UserSendDate userSendDate;
    private RecyclerView recyclerView;
    private String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_menu);
        recyclerView = findViewById(R.id.orderInfoProfileList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        profileButton = findViewById(R.id.profileButton);
        userId = getDate(savedInstanceState, "userId");
        driverCarRec = findViewById(R.id.buttonCarRec);
        driverCarRec.setOnClickListener(view -> goCarRec());
        profileButton.setOnClickListener(view -> goDriverProfile());
        getOrders();
        new Thread(() -> {
            try {
                Log.d("Driver Menu", "Run");
                WebSocketClient driverClient = new WebSocketClient();
                ListenableFuture<StompSession> f = driverClient.connect();
                stompSession = f.get();
                System.out.println(userId);
                subscribeDriver(Integer.parseInt(userId));
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void goCarRec(){
        Intent intent = new Intent(this, DriverCarRecommendationsActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    public void getOrders(){
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi orderInfoProfileApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        orderInfoProfileApi.getOderInfoDriver(userId)
                .enqueue(new Callback<List<OrderInfo>>() {
                    @Override
                    public void onResponse(Call<List<OrderInfo>> call, Response<List<OrderInfo>> response) {
                        populateListView(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<OrderInfo>> call, Throwable t) {
                        Toast.makeText(DriverMenuActivity.this, "Failed to load orders info",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void populateListView(List<OrderInfo> body) {
       OrderInfoProfileAdapter orderInfoProfileAdapter = new OrderInfoProfileAdapter(body, this);
       recyclerView.setAdapter(orderInfoProfileAdapter);
    }

    private void goDriverProfile() {
        Intent intent = new Intent(DriverMenuActivity.this, DriverProfile.class);
        System.out.println("id " + userId);
        intent.putExtra("driverId", userId);
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

    public void subscribeDriver(int userID) throws ExecutionException, InterruptedException {
        stompSession.subscribe("/user/" + userID + "/driver", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return byte[].class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object o) {
                Log.d("Driver Menu(driver-message) Received ", new String((byte[]) o));
                try {
                    userSendDate = parseUserSendDateFromString( new String((byte[]) o));
                    showOrderInfo(userSendDate);
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
                Log.d("Driver Menu(order-accept) Received ", new String((byte[]) o));
            }
        });
    }

    public void showOrderInfo(UserSendDate userSendDate){
        runOnUiThread(() -> {
            dialogBuilder =new AlertDialog.Builder(DriverMenuActivity.this);
            final View contactPopupView = getLayoutInflater().inflate(R.layout.popwindow, null);
            initialize(contactPopupView);
            customerName.setText(userSendDate.getCustomerName());
            addressCustomer.setText(userSendDate.getAddressCustomer());
            addressDelivery.setText(userSendDate.getAddressDelivery());
            telephoneCustomer.setText(userSendDate.getTelephoneNumber());
            dialogBuilder.setView(contactPopupView);
            dialog = dialogBuilder.create();
            dialog.show();
        });
    }

    public void initialize(View contactPopupView){
        customerName = contactPopupView.findViewById(R.id.driverNameCustomerText);
        addressCustomer = contactPopupView.findViewById(R.id.driverAddressCustomerText);
        addressDelivery = contactPopupView.findViewById(R.id.driverAddressDeliveryText);
        telephoneCustomer = contactPopupView.findViewById(R.id.driverTelephoneCustomerText);
        accept = contactPopupView.findViewById(R.id.acceptButton);
        cancel = contactPopupView.findViewById(R.id.cancelButton);
        accept.setOnClickListener(view -> accept());
        cancel.setOnClickListener(view -> cancel());
    }

    public void accept(){
        new Thread(() -> {
            ResponseMessage responseMessage = new ResponseMessage(String.valueOf(userSendDate.getUserId()), "Yes");
            sendDriverAcceptMessage(responseMessage);
        }).start();
        goCurrentOrderInfo();
    }

    public void cancel(){
        new Thread(() -> {
            ResponseMessage responseMessage = new ResponseMessage(String.valueOf(userSendDate.getUserId()), "No");
            sendDriverAcceptMessage(responseMessage);
        }).start();

    }



    public void sendDriverAcceptMessage(ResponseMessage responseMessage) {
        System.out.println(responseMessage);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("{ \"userId\" : \"").append(responseMessage.getUserId()).append("\",")
                .append("\"content\" : \"").append(responseMessage.getContent()).append("\" }");
        stompSession.send("/app/order-accept-message", stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
    }

    private void goCurrentOrderInfo() {
        Intent intent = new Intent(this, CurrentOrderActivity.class);
        intent.putExtra("driverId", String.valueOf(userSendDate.getDriverId()));
        intent.putExtra("userId", String.valueOf(userSendDate.getUserId()));
        intent.putExtra("customerName", userSendDate.getCustomerName());
        intent.putExtra("customerAddress", userSendDate.getAddressCustomer());
        intent.putExtra("deliveryAddress", userSendDate.getAddressDelivery());
        intent.putExtra("telephoneNumber", userSendDate.getTelephoneNumber());
        startActivity(intent);
    }

    @Override
    public void onItemClicked(UserPickCar userPickCar) {

    }

    @Override
    public void onItemClicked(OrderInfo orderInfo) {
        goOrderFullInfo(orderInfo);
    }

    @Override
    public void onItemClicked(FavouriteDriverUserInfo favouriteDriverUserInfo) {

    }

    @Override
    public void onItemClicked(FavouriteAddressesUserInfo favouriteAddressesUserInfo) {

    }

    @Override
    public void onItemClicked(UserOrderInfo userOrderInfo) {

    }

    public void goOrderFullInfo(OrderInfo orderInfo){
        Intent intent = new Intent(this, OrderInfoActivity.class);
        intent.putExtra("driverId",  userId);
        intent.putExtra("date", String.valueOf(orderInfo.getDate()));
        intent.putExtra("addressCustomer", orderInfo.getAddressCustomer());
        intent.putExtra("addressDelivery", orderInfo.getAddressDelivery());
        intent.putExtra("telephoneCustomer", orderInfo.getTelephoneCustomer());
        intent.putExtra("price", String.valueOf(orderInfo.getPrice()));
        intent.putExtra("rating", String.valueOf(orderInfo.getRating()));
        intent.putExtra("numberOfKilometers", String.valueOf(orderInfo.getNumberOfKilometers()));
        intent.putExtra("customerName", orderInfo.getCustomerName());
        startActivity(intent);
    }
}
