package com.example.minitaxiandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.minitaxiandroid.R;
import com.example.minitaxiandroid.entities.document.DRIVER_STATUS;
import com.example.minitaxiandroid.entities.messages.PriceByClass;
import com.example.minitaxiandroid.entities.messages.ResponseMessage;
import com.example.minitaxiandroid.entities.messages.UserSendDate;
import com.example.minitaxiandroid.entities.ranks.Rank;
import com.example.minitaxiandroid.entities.ranks.UserEliteRankAchievementInfo;
import com.example.minitaxiandroid.entities.ranks.UserRankAchievementInfo;
import com.example.minitaxiandroid.entities.userinfo.DriverInfo;
import com.example.minitaxiandroid.retrofit.MiniTaxiApi;
import com.example.minitaxiandroid.retrofit.RetrofitService;
import com.example.minitaxiandroid.services.GFG;
import com.example.minitaxiandroid.services.UserLoginInfoService;
import com.example.minitaxiandroid.websocket.WebSocketClient;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;
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

import static com.example.minitaxiandroid.services.ObjectParserService.parseResponseMessageFromString;

;

public class MakeOrderActivity extends AppCompatActivity {
    private Button makeOrderButton, cancelMakeOrderButton;
    private TextView priceStandardTextView, priceComfortTextView, priceEliteTextView, makeOrderBonusesTextView,
            saleBonusesTextView, freeOrderBonusesTextView;
    private EditText nameEditText, phoneEditText;
    private CardView saleBonusesCardView, standardBonusesCardView, comfortBonusesCardView, eliteBonusesCardView;
    private RadioButton standardRadioButton, comfortRadioButton, eliteRadioButton, saleRadioButton,
            standardFreeOrdersRadioButton, comfortFreeOrdersRadioButton, eliteFreeOrdersRadioButton;
    private DriverResponseDialog dialog;
    private String userId, userAddressFrom, userAddressTo, latitude, longitude;
    private Rank userRank;
    private int driverId;
    private int classId;
    private StompSession stompSession;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);
        makeOrderButton = findViewById(R.id.makeOrderButton);
        cancelMakeOrderButton = findViewById(R.id.cancelMakeOrderButton);
        priceStandardTextView = findViewById(R.id.priceStandardTextView);
        priceComfortTextView = findViewById(R.id.priceComfortTextView);
        priceEliteTextView = findViewById(R.id.priceEliteTextView);
        makeOrderBonusesTextView = findViewById(R.id.makeOrderBonusesTextView);
        nameEditText = findViewById(R.id.userEditTextTextPersonName);
        phoneEditText = findViewById(R.id.userEditTextPhone);
        freeOrderBonusesTextView = findViewById(R.id.freeOrderBonusesTextView);
        saleBonusesTextView = findViewById(R.id.saleBonusesTextView);
        saleBonusesCardView = findViewById(R.id.saleBonusesCardView);
        standardBonusesCardView = findViewById(R.id.standardBonusesCardView);
        comfortBonusesCardView = findViewById(R.id.comfortBonusesCardView);
        eliteBonusesCardView = findViewById(R.id.eliteBonusesCardView);
        initializeRadioButtons();
        userAddressFrom = getDate(savedInstanceState, "userAddressFrom");
        userAddressTo = getDate(savedInstanceState, "userAddressTo");
        latitude = getDate(savedInstanceState, "latitude");
        longitude = getDate(savedInstanceState, "longitude");
//        driverId = Integer.parseInt(getDate(savedInstanceState, "driverId"));
        driverId = 0;
        UserLoginInfoService.init(MakeOrderActivity.this);
        UserLoginInfoService.addProperty("userId", "1");
        UserLoginInfoService.addProperty("rankId", "6");
        getRanksInfoRequest();
        getUserOrderPriceByClass();
        userId = UserLoginInfoService.getProperty("userId");
        dialog = new DriverResponseDialog();
        makeOrderButton.setOnClickListener(view -> {
            makeOrder();
        });
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://energy-taxi-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();
        cancelMakeOrderButton.setOnClickListener(view -> goMain());
    }

    public void makeOrder() {
        if(isValidUserInformation()) {
            if(driverId > 0){
                subscribeAndSendDataToDriver(driverId);
                showWaitForDriverResponse();
            }
            else {
                DatabaseReference uidRef = databaseReference.child("drivers-info");
                Query query = uidRef.orderByChild("status").equalTo(DRIVER_STATUS.ONLINE.name());
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DataSnapshot firstDriver = dataSnapshot.getChildren().iterator().next();
                        DriverInfo nearestDriver = firstDriver.getValue(DriverInfo.class);
                        double minDistance = GFG.distance(nearestDriver.getLatitude(), Double.parseDouble(latitude),
                                nearestDriver.getLongitude(), Double.parseDouble(longitude));
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            DriverInfo driverInfo = ds.getValue(DriverInfo.class);
                            double currentDistance = GFG.distance(driverInfo.getLatitude(),
                                    Double.parseDouble("50.1"), driverInfo.getLongitude(),
                                    Double.parseDouble("24.4"));
                            if (minDistance > currentDistance) {
                                minDistance = currentDistance;
                                nearestDriver = driverInfo;
                            }
                        }
                        subscribeAndSendDataToDriver(nearestDriver.getDriverId());
                        showWaitForDriverResponse();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("TAG", databaseError.getMessage());
                    }
                };
                query.addListenerForSingleValueEvent(valueEventListener);
            }
        }
        else{
            Toast.makeText(MakeOrderActivity.this,
                    getResources().getString(R.string.error_find_driver), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isValidUserInformation(){
        if(nameEditText.getText().toString().isEmpty()){
            nameEditText.setText(UserLoginInfoService.getProperty("username"));
        }
        if(phoneEditText.getText().toString().isEmpty()){
            phoneEditText.setError(getResources().getString(R.string.please_enter_phone));
            return false;
        }
        else {
            return true;
        }
    }
    public void subscribeAndSendDataToDriver(int driverId){
        new Thread(() -> {
            try {
                Log.d("MakeOrder", "Run");
                WebSocketClient userClient = new WebSocketClient();
                ListenableFuture<StompSession> f = userClient.connect();
                stompSession = f.get();
                UserSendDate userSendDate = new UserSendDate(driverId,
                        Integer.parseInt(userId), nameEditText.getText().toString(),
                        userAddressFrom, userAddressTo, phoneEditText.getText().toString());
                subscribeUser(Integer.parseInt(userId));
                sendUserDateMessage(stompSession, userSendDate);
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
    public void initializeRadioButtons(){
        standardRadioButton = findViewById(R.id.standardRadioButton);
        standardRadioButton.setOnClickListener(view ->  {
            if (standardRadioButton.isSelected()) {
                standardRadioButton.setSelected(false);
                standardRadioButton.setChecked(false);
                saleRadioButton.setSelected(false);
                saleRadioButton.setChecked(false);

            } else {
                classId = standardRadioButton.getId();
                standardRadioButton.setSelected(true);
                standardRadioButton.setChecked(true);
                comfortRadioButton.setSelected(false);
                comfortRadioButton.setChecked(false);
                eliteRadioButton.setSelected(false);
                eliteRadioButton.setChecked(false);
                standardFreeOrdersRadioButton.setSelected(false);
                standardFreeOrdersRadioButton.setChecked(false);
                comfortFreeOrdersRadioButton.setSelected(false);
                comfortFreeOrdersRadioButton.setChecked(false);
                eliteFreeOrdersRadioButton.setSelected(false);
                eliteFreeOrdersRadioButton.setChecked(false);
            }
        });
        comfortRadioButton = findViewById(R.id.comfortRadioButton);
        comfortRadioButton.setOnClickListener(view -> {
            if(comfortRadioButton.isSelected()){
                comfortRadioButton.setSelected(false);
                comfortRadioButton.setChecked(false);
                saleRadioButton.setSelected(false);
                saleRadioButton.setChecked(false);
            }
            else{
                classId = comfortRadioButton.getId();
                comfortRadioButton.setSelected(true);
                comfortRadioButton.setSelected(true);
                standardRadioButton.setSelected(false);
                standardRadioButton.setChecked(false);
                eliteRadioButton.setSelected(false);
                eliteRadioButton.setChecked(false);
                standardFreeOrdersRadioButton.setSelected(false);
                standardFreeOrdersRadioButton.setChecked(false);
                comfortFreeOrdersRadioButton.setSelected(false);
                comfortFreeOrdersRadioButton.setChecked(false);
                eliteFreeOrdersRadioButton.setSelected(false);
                eliteFreeOrdersRadioButton.setChecked(false);
            }
        });
        eliteRadioButton = findViewById(R.id.eliteRadioButton);
        eliteRadioButton.setOnClickListener(view -> {
            if(eliteRadioButton.isSelected()){
                eliteRadioButton.setSelected(false);
                eliteRadioButton.setChecked(false);
                saleRadioButton.setSelected(false);
                saleRadioButton.setChecked(false);
            }
            else{
                classId = eliteRadioButton.getId();
                standardRadioButton.setSelected(false);
                standardRadioButton.setChecked(false);
                comfortRadioButton.setSelected(false);
                comfortRadioButton.setChecked(false);
                eliteRadioButton.setSelected(true);
                eliteRadioButton.setChecked(true);
                standardFreeOrdersRadioButton.setSelected(false);
                standardFreeOrdersRadioButton.setChecked(false);
                comfortFreeOrdersRadioButton.setSelected(false);
                comfortFreeOrdersRadioButton.setChecked(false);
                eliteFreeOrdersRadioButton.setSelected(false);
                eliteFreeOrdersRadioButton.setChecked(false);
            }
        });
        saleRadioButton = findViewById(R.id.saleRadioButton);
        saleRadioButton.setOnClickListener(view -> {
            if(saleRadioButton.isSelected()){
                saleRadioButton.setSelected(false);
                saleRadioButton.setChecked(false);
            }
            else{
                saleRadioButton.setSelected(true);
                saleRadioButton.setChecked(true);
                standardFreeOrdersRadioButton.setSelected(false);
                standardFreeOrdersRadioButton.setChecked(false);
                comfortFreeOrdersRadioButton.setSelected(false);
                comfortFreeOrdersRadioButton.setChecked(false);
                eliteFreeOrdersRadioButton.setSelected(false);
                eliteFreeOrdersRadioButton.setChecked(false);
            }
        });
        standardFreeOrdersRadioButton = findViewById(R.id.standardFreeOrdersRadioButton);
        standardFreeOrdersRadioButton.setOnClickListener(view -> {
            if(standardFreeOrdersRadioButton.isSelected()){
                standardFreeOrdersRadioButton.setSelected(false);
                standardFreeOrdersRadioButton.setChecked(false);
            }
            else{
                classId = standardFreeOrdersRadioButton.getId();
                standardRadioButton.setSelected(false);
                standardRadioButton.setChecked(false);
                comfortRadioButton.setSelected(false);
                comfortRadioButton.setChecked(false);
                eliteRadioButton.setSelected(false);
                eliteRadioButton.setChecked(false);
                saleRadioButton.setSelected(false);
                saleRadioButton.setChecked(false);
                standardFreeOrdersRadioButton.setSelected(true);
                standardFreeOrdersRadioButton.setChecked(true);
                comfortFreeOrdersRadioButton.setSelected(false);
                comfortFreeOrdersRadioButton.setChecked(false);
                eliteFreeOrdersRadioButton.setSelected(false);
                eliteFreeOrdersRadioButton.setChecked(false);
            }
        });
        comfortFreeOrdersRadioButton = findViewById(R.id.comfortFreeOrdersRadioButton);
        comfortFreeOrdersRadioButton.setOnClickListener(view -> {
            if(comfortFreeOrdersRadioButton.isSelected()){
                comfortFreeOrdersRadioButton.setSelected(false);
                comfortFreeOrdersRadioButton.setChecked(false);
            }
            else{
                classId = comfortFreeOrdersRadioButton.getId();
                standardRadioButton.setSelected(false);
                standardRadioButton.setChecked(false);
                comfortRadioButton.setSelected(false);
                comfortRadioButton.setChecked(false);
                eliteRadioButton.setSelected(false);
                eliteRadioButton.setChecked(false);
                saleRadioButton.setSelected(false);
                saleRadioButton.setChecked(false);
                standardFreeOrdersRadioButton.setSelected(false);
                standardFreeOrdersRadioButton.setChecked(false);
                comfortFreeOrdersRadioButton.setSelected(true);
                comfortFreeOrdersRadioButton.setChecked(true);
                eliteFreeOrdersRadioButton.setSelected(false);
                eliteFreeOrdersRadioButton.setChecked(false);
            }
        });
        eliteFreeOrdersRadioButton = findViewById(R.id.eliteFreeOrdersRadioButton);
        eliteFreeOrdersRadioButton.setOnClickListener(view -> {
            if(eliteFreeOrdersRadioButton.isSelected()){
                eliteFreeOrdersRadioButton.setSelected(false);
                eliteFreeOrdersRadioButton.setChecked(false);
            }
            else{
                classId = eliteFreeOrdersRadioButton.getId();
                standardRadioButton.setSelected(false);
                standardRadioButton.setChecked(false);
                comfortRadioButton.setSelected(false);
                comfortRadioButton.setChecked(false);
                eliteRadioButton.setSelected(false);
                eliteRadioButton.setChecked(false);
                saleRadioButton.setSelected(false);
                saleRadioButton.setChecked(false);
                standardFreeOrdersRadioButton.setSelected(false);
                standardFreeOrdersRadioButton.setChecked(false);
                comfortFreeOrdersRadioButton.setSelected(false);
                comfortFreeOrdersRadioButton.setChecked(false);
                eliteFreeOrdersRadioButton.setSelected(true);
                eliteFreeOrdersRadioButton.setChecked(true);
            }
        });
    }

    public void getRanksInfoRequest(){
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi rankInfoApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        rankInfoApi.getRankInfo().enqueue(new Callback<List<Rank>>() {
            @Override
            public void onResponse(Call<List<Rank>> call, Response<List<Rank>> response) {
                getRanksInfo(response.body());
                getRanksStats();
            }

            @Override
            public void onFailure(Call<List<Rank>> call, Throwable t) {
                Toast.makeText(MakeOrderActivity.this, "Failed to load ranks info",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getRanksInfo(List<Rank> rankList){
        for(Rank rank: rankList){
            if(rank.getRankId() == Integer.parseInt(UserLoginInfoService.getProperty("rankId"))){
                this.userRank = rank;
                break;
            }
        }
    }

    private void getRanksStats(){
        if(Integer.parseInt(UserLoginInfoService.getProperty("rankId")) > 1){
            getBaseRankUserStatsRequest();
            if(Integer.parseInt(UserLoginInfoService.getProperty("rankId"))>4){
                if(driverId > 0){
                    getEliteRankUserStatsRequestByDriver();
                }
                else {
                    getEliteRankUserStatsRequest();
                }
            }
        }
    }

    private void getEliteRankUserStatsRequestByDriver(){
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi rankInfoApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        rankInfoApi.getUserEliteRankAchievementsInfoByDriver(Integer.parseInt(UserLoginInfoService.getProperty("userId")),
                        Integer.parseInt(UserLoginInfoService.getProperty("rankId")), driverId)
                .enqueue(new Callback<List<UserEliteRankAchievementInfo>>() {

                    @Override
                    public void onResponse(Call<List<UserEliteRankAchievementInfo>> call,
                                           Response<List<UserEliteRankAchievementInfo>> response) {
                        setEliteRanksStats(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<UserEliteRankAchievementInfo>> call, Throwable t) {
                        Toast.makeText(MakeOrderActivity.this,
                                getResources().getString(R.string.error_to_get_base_rank_user_info),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getBaseRankUserStatsRequest(){
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi rankInfoApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        rankInfoApi.getUserRankAchievementsInfo(Integer.parseInt(UserLoginInfoService.getProperty("userId")),
                        Integer.parseInt(UserLoginInfoService.getProperty("rankId")))
                .enqueue(new Callback<UserRankAchievementInfo>() {

                    @Override
                    public void onResponse(Call<UserRankAchievementInfo> call, Response<UserRankAchievementInfo> response) {
                        setBaseRankStats(response.body());
                    }

                    @Override
                    public void onFailure(Call<UserRankAchievementInfo> call, Throwable t) {
                        Toast.makeText(MakeOrderActivity.this,
                                getResources().getString(R.string.error_to_get_base_rank_user_info),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setBaseRankStats(UserRankAchievementInfo response){
        int countOfSaleOrders = response.getNumberOfUsesSale();
        if(countOfSaleOrders != 0){
            makeOrderBonusesTextView.setVisibility(View.VISIBLE);
            saleBonusesCardView.setVisibility(View.VISIBLE);
            String str = saleBonusesTextView.getText().toString() + " (" +userRank.getSaleValue() + "%) ";
            saleBonusesTextView.setText(str);
        }
    }

    public void getEliteRankUserStatsRequest(){
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi rankInfoApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        rankInfoApi.getUserEliteRankAchievementsInfo(Integer.parseInt(UserLoginInfoService.getProperty("userId")),
                        Integer.parseInt(UserLoginInfoService.getProperty("rankId")))
                .enqueue(new Callback<List<UserEliteRankAchievementInfo>>() {

                    @Override
                    public void onResponse(Call<List<UserEliteRankAchievementInfo>> call,
                                           Response<List<UserEliteRankAchievementInfo>> response) {
                        setEliteRanksStats(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<UserEliteRankAchievementInfo>> call, Throwable t) {
                        Toast.makeText(MakeOrderActivity.this,
                                getResources().getString(R.string.error_to_get_base_rank_user_info),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setEliteRanksStats(List<UserEliteRankAchievementInfo> userEliteRankAchievementInfoList){
        if(userEliteRankAchievementInfoList.size() == 1
                && userEliteRankAchievementInfoList.get(0).getNumberOfUsesFreeOrder() != 0){
            freeOrderBonusesTextView.setVisibility(View.VISIBLE);
            standardBonusesCardView.setVisibility(View.VISIBLE);
        }
        else if(userEliteRankAchievementInfoList.size() == 2){
            for(UserEliteRankAchievementInfo userEliteRankAchievementInfo: userEliteRankAchievementInfoList){
                checkUserNumberOfOrdersOnNull(userEliteRankAchievementInfo);
            }
        }
        else{
            for(UserEliteRankAchievementInfo userEliteRankAchievementInfo: userEliteRankAchievementInfoList){
                checkUserNumberOfOrdersOnNull(userEliteRankAchievementInfo);
                if(userEliteRankAchievementInfo.getCarClassId() == 3
                        && userEliteRankAchievementInfo.getNumberOfUsesFreeOrder() != 0){
                    freeOrderBonusesTextView.setVisibility(View.VISIBLE);
                    eliteBonusesCardView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void checkUserNumberOfOrdersOnNull(UserEliteRankAchievementInfo userEliteRankAchievementInfo) {
        if(userEliteRankAchievementInfo.getCarClassId() == 1
                && userEliteRankAchievementInfo.getNumberOfUsesFreeOrder() != 0){
            freeOrderBonusesTextView.setVisibility(View.VISIBLE);
            standardBonusesCardView.setVisibility(View.VISIBLE);
        }
        if(userEliteRankAchievementInfo.getCarClassId() == 2
                && userEliteRankAchievementInfo.getNumberOfUsesFreeOrder() != 0){
            freeOrderBonusesTextView.setVisibility(View.VISIBLE);
            comfortBonusesCardView.setVisibility(View.VISIBLE);
        }
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

    private void getUserOrderPriceByClass(){
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi rankInfoApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        rankInfoApi.getUserOrderPriceByClass(userAddressFrom, userAddressTo).enqueue(new Callback<PriceByClass>() {

                    @Override
                    public void onResponse(Call<PriceByClass> call, Response<PriceByClass> response) {
                        setTextPrice(response.body());
                    }

                    @Override
                    public void onFailure(Call<PriceByClass> call, Throwable t) {
                        Toast.makeText(MakeOrderActivity.this,
                                getResources().getString(R.string.error_to_get_user_order_price_by_class),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showWaitForDriverResponse() {
        dialog.setLayout(R.layout.user_wait_response);
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(),"wait response");
    }

    public void subscribeUser(int userID) throws ExecutionException, InterruptedException {
        stompSession.subscribe("/user/" + userID + "/driver", new StompFrameHandler() {
            @NotNull
            @Override
            public Type getPayloadType(@NotNull StompHeaders headers) {
                return byte[].class;
            }

            @Override
            public void handleFrame(@NotNull StompHeaders headers, Object o) {
                Log.d("\"Make Order Received ", new String((byte[]) o));
            }
        });

        stompSession.subscribe("/user/" + userID + "/order-accept", new StompFrameHandler() {
            @NotNull
            @Override
            public Type getPayloadType(@NotNull StompHeaders headers) {
                return byte[].class;
            }

            @Override
            public void handleFrame(@NotNull StompHeaders headers, Object o) {
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

    private void setTextPrice(PriceByClass response) {
        runOnUiThread(() -> {
            try {
                System.out.println(response.getPriceByClass().get(0));
                String str1 = "PRICE: " + response.getPriceByClass().get(0);
                String str2 = "PRICE: " + response.getPriceByClass().get(1);
                String str3 = "PRICE: " + response.getPriceByClass().get(2);
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
}
