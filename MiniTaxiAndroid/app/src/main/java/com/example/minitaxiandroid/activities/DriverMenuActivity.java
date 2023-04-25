package com.example.minitaxiandroid.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.minitaxiandroid.R;
import com.example.minitaxiandroid.adapter.OrderInfoProfileAdapter;
import com.example.minitaxiandroid.api.MiniTaxiApi;
import com.example.minitaxiandroid.api.RetrofitService;
import com.example.minitaxiandroid.api.SelectListener;
import com.example.minitaxiandroid.entities.auth.RegisterResponse;
import com.example.minitaxiandroid.entities.document.DRIVER_STATUS;
import com.example.minitaxiandroid.entities.messages.OrderInfo;
import com.example.minitaxiandroid.entities.messages.ResponseMessage;
import com.example.minitaxiandroid.entities.messages.UserPickCar;
import com.example.minitaxiandroid.entities.messages.UserSendDate;
import com.example.minitaxiandroid.entities.userinfo.FavouriteAddressesUserInfo;
import com.example.minitaxiandroid.entities.userinfo.FavouriteDriverUserInfo;
import com.example.minitaxiandroid.entities.userinfo.UserOrderInfo;
import com.example.minitaxiandroid.services.DriverInfoService;
import com.example.minitaxiandroid.services.DriverLocationService;
import com.example.minitaxiandroid.websocket.WebSocketClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.concurrent.ListenableFuture;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
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
    private FloatingActionButton logoutButton;
    private Switch isOnlineSwitch;
    private StompSession stompSession;
    private UserSendDate userSendDate;
    private RecyclerView recyclerView;
    private LocationManager locationManager;
    private DatabaseReference databaseReference;
    private DriverLocationService driverLocationService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_menu);
        recyclerView = findViewById(R.id.orderInfoProfileList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        profileButton = findViewById(R.id.driverProfileButton);
        logoutButton = findViewById(R.id.driverLogoutButton);
        isOnlineSwitch = findViewById(R.id.isOnlineSwitch);
        driverCarRec = findViewById(R.id.buttonCarRec);
        driverCarRec.setOnClickListener(view -> goCarRec());
        profileButton.setOnClickListener(view -> goDriverProfile());
        logoutButton.setOnClickListener(view -> logout());
        DriverInfoService.init(DriverMenuActivity.this);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(
                "https://energy-taxi-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        driverLocationService = new DriverLocationService(Integer.parseInt(DriverInfoService.getProperty("driverId")),
                DRIVER_STATUS.OFFLINE, databaseReference);
        String isOnline = getDate(savedInstanceState, "isOnline");
        if(isOnline != null){
            if(isOnline.equals("true")){
                setDriverStatus(DRIVER_STATUS.ONLINE);
                driverLocationService.setDriverStatus(DRIVER_STATUS.ONLINE);
                isOnlineSwitch.setChecked(true);
                connect();
            }
            else {
                setDriverStatus(DRIVER_STATUS.OFFLINE);
                driverLocationService.setDriverStatus(DRIVER_STATUS.OFFLINE);
                isOnlineSwitch.setChecked(false);
            }
        }
        else {
            setDriverStatus(DRIVER_STATUS.OFFLINE);
            driverLocationService.setDriverStatus(DRIVER_STATUS.OFFLINE);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 15,
                driverLocationService);
        isOnlineSwitch.setOnClickListener(view -> {
            if(isOnlineSwitch.isChecked()){
                setDriverStatus(DRIVER_STATUS.ONLINE);
                driverLocationService.setDriverStatus(DRIVER_STATUS.ONLINE);
                connect();
            }
            else {
                setDriverStatus(DRIVER_STATUS.OFFLINE);
                driverLocationService.setDriverStatus(DRIVER_STATUS.OFFLINE);
                if(stompSession!=null && stompSession.isConnected()) {
                    new Thread(() -> stompSession.disconnect()).start();
                }
            }
        });
        getOrders();
    }

    private void connect(){
        Log.d("Driver Menu", "Run");
        new Thread(() -> {
            try {
                WebSocketClient driverClient = new WebSocketClient();
                ListenableFuture<StompSession> f = driverClient.connect();
                stompSession = f.get();
                subscribeDriver();
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private void setDriverStatus(DRIVER_STATUS driverStatus){
        String id = "driver-"+ DriverInfoService.getProperty("driverId");
        databaseReference.child("drivers-info").child(id).child("status").
                setValue(driverStatus.name()).addOnCompleteListener(task -> {
                    if(task.isComplete()){
                        Log.d("TAG", "Driver status successfully changed");
                        Toast.makeText(DriverMenuActivity.this, "Driver status successfully changed",
                                Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Log.d("TAG", "Error to change driver status");
                        Toast.makeText(DriverMenuActivity.this,
                                "Error to change driver status", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void goCarRec(){
        Intent intent = new Intent(this, DriverCarRecommendationsActivity.class);
        intent.putExtra("driverId", DriverInfoService.getProperty("driverId"));
        intent.putExtra("isOnline", String.valueOf(isOnlineSwitch.isChecked()));
        isOnlineSwitch.setChecked(false);
        if(stompSession!=null && stompSession.isConnected()) {
            new Thread(() -> stompSession.disconnect()).start();
        }
        startActivity(intent);
    }

    public void getOrders(){
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi orderInfoProfileApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        orderInfoProfileApi.getOderInfoDriver("Bearer " + DriverInfoService.getProperty("access_token"),
                        DriverInfoService.getProperty("driverId"))
                .enqueue(new Callback<List<OrderInfo>>() {
                    @Override
                    public void onResponse(Call<List<OrderInfo>> call, Response<List<OrderInfo>> response) {
                        if(response.body() != null){
                            try {
                                if(response.errorBody() != null){
                                    if(response.errorBody().string().contains(getResources()
                                            .getString(R.string.token_expired))){
                                        refreshToken();
                                    }
                                }
                                else{
                                    populateListView(response.body());
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<List<OrderInfo>> call, Throwable t) {
                        DriverMenuActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(DriverMenuActivity.this, "Failed to load orders info",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    private void populateListView(List<OrderInfo> body) {
        OrderInfoProfileAdapter orderInfoProfileAdapter = new OrderInfoProfileAdapter(body, this);
        recyclerView.setAdapter(orderInfoProfileAdapter);
    }
    private void goDriverProfile() {
        Intent intent = new Intent(DriverMenuActivity.this, DriverProfileActivity.class);
        intent.putExtra("driverId", DriverInfoService.getProperty("driverId"));
        intent.putExtra("isOnline", String.valueOf(isOnlineSwitch.isChecked()));
        isOnlineSwitch.setChecked(false);
        if(stompSession!=null && stompSession.isConnected()) {
            new Thread(() -> stompSession.disconnect()).start();
        }
        startActivity(intent);
    }
    public void subscribeDriver() throws ExecutionException, InterruptedException {
        stompSession.subscribe("/user/" + DriverInfoService.getProperty("driverId") + "/driver", new StompFrameHandler() {
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
        stompSession.subscribe("/user/" + DriverInfoService.getProperty("driverId") + "/order-accept", new StompFrameHandler() {
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
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.card_view_border_reverse, getTheme()));
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
            if(stompSession!=null && stompSession.isConnected()) {
                new Thread(() -> stompSession.disconnect()).start();
            }
        }).start();
        runOnUiThread(() -> {
            dialog.cancel();
        });
        setDriverStatus(DRIVER_STATUS.IN_ORDER);
        goCurrentOrderInfo();
    }
    public void cancel(){
        new Thread(() -> {
            ResponseMessage responseMessage = new ResponseMessage(String.valueOf(userSendDate.getUserId()), "No");
            sendDriverAcceptMessage(responseMessage);
        }).start();
        runOnUiThread(() -> {
            dialog.cancel();
        });
    }
    public void sendDriverAcceptMessage(ResponseMessage responseMessage) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("{ \"userId\" : \"").append(responseMessage.getUserId()).append("\",")
                .append("\"content\" : \"").append(responseMessage.getContent()).append("\" }");
        stompSession.send("/app/driver-accept-order-message", stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
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
    private void goLogin() {
        Intent intent = new Intent(this, UserLoginActivity.class);
        setDriverStatus(DRIVER_STATUS.OFFLINE);
        isOnlineSwitch.setChecked(false);
        if(stompSession!=null && stompSession.isConnected()) {
            new Thread(() -> stompSession.disconnect()).start();
        }
        startActivity(intent);
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
                                if(stompSession!=null && stompSession.isConnected()) {
                                    new Thread(() -> stompSession.disconnect()).start();
                                }
                                setDriverStatus(DRIVER_STATUS.OFFLINE);
                                driverLocationService.setDriverStatus(DRIVER_STATUS.OFFLINE);
                                isOnlineSwitch.setChecked(false);
                                finish();
                                startActivity(getIntent());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        DriverMenuActivity.this.runOnUiThread(()
                                -> Toast.makeText(DriverMenuActivity.this, "Failed to check user authentication",
                                Toast.LENGTH_SHORT).show());

                    }
                });
    }
    private void logout() {
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi api = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        api.logout("Bearer " + DriverInfoService.getProperty("access_token"))
                .enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<RegisterResponse> call,
                                           @NotNull Response<RegisterResponse> response) {
                        if(response.body()!=null){
                            try {
                                if(response.errorBody() != null){
                                    if(response.errorBody().string().contains(getResources()
                                            .getString(R.string.token_expired))){
                                        refreshToken();
                                    }
                                }
                                else{
                                    DriverInfoService.clear();
                                    goLogin();
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<RegisterResponse> call, Throwable t) {
                        DriverMenuActivity.this.runOnUiThread(() ->
                                Toast.makeText(DriverMenuActivity.this, "Failed to logout",
                                Toast.LENGTH_SHORT).show());

                    }
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
        intent.putExtra("driverId",  DriverInfoService.getProperty("driverId"));
        intent.putExtra("date", String.valueOf(orderInfo.getDate()));
        intent.putExtra("addressCustomer", orderInfo.getAddressCustomer());
        intent.putExtra("addressDelivery", orderInfo.getAddressDelivery());
        intent.putExtra("telephoneCustomer", orderInfo.getTelephoneCustomer());
        intent.putExtra("price", String.valueOf(orderInfo.getPrice()));
        intent.putExtra("rating", String.valueOf(orderInfo.getRating()));
        intent.putExtra("numberOfKilometers", String.valueOf(orderInfo.getNumberOfKilometers()));
        intent.putExtra("customerName", orderInfo.getCustomerName());
        intent.putExtra("isOnline", String.valueOf(isOnlineSwitch.isChecked()));
        isOnlineSwitch.setChecked(false);
        if(stompSession!=null && stompSession.isConnected()) {
            new Thread(() -> stompSession.disconnect()).start();
        }
        startActivity(intent);
    }

}
