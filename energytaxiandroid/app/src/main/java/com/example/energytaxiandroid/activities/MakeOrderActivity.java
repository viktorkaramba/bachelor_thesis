package com.example.energytaxiandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.energytaxiandroid.R;
import com.example.energytaxiandroid.api.MiniTaxiApi;
import com.example.energytaxiandroid.api.RetrofitService;
import com.example.energytaxiandroid.entities.auth.RegisterResponse;
import com.example.energytaxiandroid.entities.document.CAR_CLASSES;
import com.example.energytaxiandroid.entities.document.DRIVER_STATUS;
import com.example.energytaxiandroid.entities.messages.PriceByClassRequest;
import com.example.energytaxiandroid.entities.messages.PriceByClassResponse;
import com.example.energytaxiandroid.entities.messages.ResponseMessage;
import com.example.energytaxiandroid.entities.messages.UserSendDate;
import com.example.energytaxiandroid.entities.ranks.Rank;
import com.example.energytaxiandroid.entities.ranks.UserEliteRankAchievementInfo;
import com.example.energytaxiandroid.entities.ranks.UserRankAchievementInfo;
import com.example.energytaxiandroid.entities.userinfo.DriverInfo;
import com.example.energytaxiandroid.services.GFG;
import com.example.energytaxiandroid.services.UserInfoService;
import com.example.energytaxiandroid.websocket.WebSocketClient;
import com.google.firebase.database.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.energytaxiandroid.services.ObjectParserService.parseResponseMessageFromString;


public class MakeOrderActivity extends AppCompatActivity {
    private Button makeOrderButton, cancelMakeOrderButton;
    private TextView priceStandardTextView, priceComfortTextView, priceEliteTextView, makeOrderBonusesTextView,
            saleBonusesTextView, freeOrderBonusesTextView;
    private EditText nameEditText, phoneEditText;
    private CardView standardCardView, comfortCardView, eliteCardView, saleBonusesCardView,
            standardBonusesCardView, comfortBonusesCardView, eliteBonusesCardView;
    private RadioButton standardRadioButton, comfortRadioButton, eliteRadioButton, saleRadioButton,
            standardFreeOrdersRadioButton, comfortFreeOrdersRadioButton, eliteFreeOrdersRadioButton;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private String userId, userAddressFrom, userAddressTo, latitude, longitude;
    private Rank userRank;
    private int favouriteDriverId, currentDriverId = 0;
    private List<Integer> noAnswerDrivers;
    private boolean isGetAnswer = false, isFavouriteDriver = false;
    private CAR_CLASSES carClass = null;
    private DriverInfo selectedDriver;
    private PriceByClassResponse priceByClassResponse;
    private float distance, priceStandard, priceComfort, priceElite, currentPrice;
    private StompSession stompSession;
    private DatabaseReference databaseReference;
    private boolean allow = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);
        makeOrderButton = findViewById(R.id.makeOrderButton);
        cancelMakeOrderButton = findViewById(R.id.cancelMakeOrderButton);
        priceStandardTextView = findViewById(R.id.priceStandardTextView);
        standardCardView = findViewById(R.id.standardCardView);
        priceComfortTextView = findViewById(R.id.priceComfortTextView);
        comfortCardView = findViewById(R.id.comfortCardView);
        priceEliteTextView = findViewById(R.id.priceEliteTextView);
        eliteCardView = findViewById(R.id.eliteCardView);
        makeOrderBonusesTextView = findViewById(R.id.makeOrderBonusesTextView);
        nameEditText = findViewById(R.id.userEditTextTextPersonName);
        phoneEditText = findViewById(R.id.userEditTextPhone);
        freeOrderBonusesTextView = findViewById(R.id.freeOrderBonusesTextView);
        saleBonusesTextView = findViewById(R.id.saleBonusesTextView);
        saleBonusesCardView = findViewById(R.id.saleBonusesCardView);
        standardBonusesCardView = findViewById(R.id.standardBonusesCardView);
        comfortBonusesCardView = findViewById(R.id.comfortBonusesCardView);
        eliteBonusesCardView = findViewById(R.id.eliteBonusesCardView);
        noAnswerDrivers = new ArrayList<>();
        initializeRadioButtons();
        userAddressFrom = getDate(savedInstanceState, "userAddressFrom");
        userAddressTo = getDate(savedInstanceState, "userAddressTo");
        latitude = getDate(savedInstanceState, "latitude");
        longitude = getDate(savedInstanceState, "longitude");
        favouriteDriverId = Integer.parseInt(getDate(savedInstanceState, "driverId"));
        distance = Float.parseFloat(getDate(savedInstanceState, "distance"));
        carClass = CAR_CLASSES.valueOf(getDate(savedInstanceState, "carClass"));
        UserInfoService.init(MakeOrderActivity.this);
        getRanksInfoRequest();
        getUserOrderPriceByClass();
        userId = UserInfoService.getProperty("userId");
        makeOrderButton.setOnClickListener(view -> {
            makeOrder();
        });
        setCarClass();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(
                "https://energy-taxi-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();
        cancelMakeOrderButton.setOnClickListener(view -> goMain());
    }

    private void setCarClass() {
        if(carClass.equals(CAR_CLASSES.STANDARD)){
            standardRadioButton.setSelected(true);
            standardRadioButton.setChecked(true);
            standardRadioButton.setClickable(false);
            currentPrice = priceStandard;
            comfortCardView.setVisibility(View.GONE);
            eliteCardView.setVisibility(View.GONE);
        }
        else if(carClass.equals(CAR_CLASSES.COMFORT)){
            comfortRadioButton.setSelected(true);
            comfortRadioButton.setChecked(true);
            comfortRadioButton.setClickable(false);
            currentPrice = priceComfort;
            standardCardView.setVisibility(View.GONE);
            eliteCardView.setVisibility(View.GONE);
        }
        else if(carClass.equals(CAR_CLASSES.ELITE)) {
            eliteRadioButton.setSelected(true);
            eliteRadioButton.setChecked(true);
            eliteRadioButton.setClickable(false);
            currentPrice = priceElite;
            standardCardView.setVisibility(View.GONE);
            comfortCardView.setVisibility(View.GONE);
        }
    }

    public void makeOrder() {
        if(isValidUserInformation()) {
            allow = true;
            isGetAnswer = false;
            if(favouriteDriverId > 0){
                if(selectedDriver == null) {
                    getSelectedDriverFromDataBase(currentDriverId);
                }
                else {
                    if(isNoAnswerDriver(selectedDriver)) {
                        isFavouriteDriver = true;
                        currentDriverId = favouriteDriverId;
                        subscribeAndSendDataToDriver(favouriteDriverId);
                        showWaitForDriverResponse();
                        setTimer(favouriteDriverId);
                    }
                }
            }
            else {
                DatabaseReference uidRef = databaseReference.child("drivers-info");
                Query query = uidRef.orderByChild("status").equalTo(DRIVER_STATUS.ONLINE.name());
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        System.out.println(dataSnapshot.exists());
                        if(dataSnapshot.getChildren().iterator().hasNext()){
                            List<DriverInfo> driverInfos = new ArrayList<>();
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                DriverInfo driverInfo = ds.getValue(DriverInfo.class);
                                if(isNoAnswerDriver(driverInfo) && driverInfo.getCarClass().equals(carClass)) {
                                   driverInfos.add(driverInfo);
                                }
                            }
                            if(driverInfos.size() == 0){
                                Toast.makeText(MakeOrderActivity.this,
                                        getResources().getString(R.string.no_driver), Toast.LENGTH_SHORT).show();
                            }
                            else{
                                DriverInfo firstDriver = driverInfos.get(0);
                                DriverInfo nearestDriver = null;
                                double minDistance = GFG.distance(firstDriver.getLatitude(), Double.parseDouble(latitude),
                                        firstDriver.getLongitude(), Double.parseDouble(longitude));
                                for (DriverInfo driverInfo : driverInfos) {
                                    double currentDistance = GFG.distance(driverInfo.getLatitude(),
                                            Double.parseDouble(latitude), driverInfo.getLongitude(),
                                            Double.parseDouble(longitude));
                                    System.out.println(minDistance + " " + currentDistance);
                                    if (minDistance >= currentDistance) {
                                        minDistance = currentDistance;
                                        nearestDriver = driverInfo;
                                    }
                                }
                                System.out.println("nearestDriver: " + nearestDriver);
                                if(nearestDriver != null) {
                                    isFavouriteDriver = false;
                                    currentDriverId = nearestDriver.getDriverId();
                                    selectedDriver = nearestDriver;
                                    subscribeAndSendDataToDriver(nearestDriver.getDriverId());
                                    showWaitForDriverResponse();
                                    setTimer(nearestDriver.getDriverId());
                                }
                            }
                        }
                        else {
                            Toast.makeText(MakeOrderActivity.this,
                                    getResources().getString(R.string.no_driver), Toast.LENGTH_SHORT).show();
                        }
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

    private void getSelectedDriverFromDataBase(int id) {
        databaseReference.child("drivers-info/driver-" + id).get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        selectedDriver = task.getResult().getValue(DriverInfo.class);
                        Log.d("firebase", String.valueOf(selectedDriver));
                        if(isNoAnswerDriver(selectedDriver)) {
                            isFavouriteDriver = true;
                            currentDriverId = favouriteDriverId;
                            subscribeAndSendDataToDriver(favouriteDriverId);
                            showWaitForDriverResponse();
                            setTimer(favouriteDriverId);
                        }
                    }
                });
    }

    private boolean isNoAnswerDriver(DriverInfo driverInfo) {
        for(Integer id: noAnswerDrivers){
            if(id == driverInfo.getDriverId()){
                return false;
            }
        }
        return true;
    }

    private void setTimer(int driverId){
        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                if(isGetAnswer){
                    this.cancel();
                }
            }
            public void onFinish() {
                noAnswerDrivers.add(driverId);
                allow = false;
                dialog.cancel();
                if(stompSession!=null && stompSession.isConnected()) {
                    new Thread(() -> stompSession.disconnect()).start();
                }
            }
        }.start();
    }

    public boolean isValidUserInformation(){
        if(carClass == null){
            Toast.makeText(MakeOrderActivity.this,
                    getResources().getString(R.string.pleas_select_class), Toast.LENGTH_SHORT).show();
            return false;
        }
        if(nameEditText.getText().toString().isEmpty()){
            nameEditText.setText(UserInfoService.getProperty("username"));
        }
        if(phoneEditText.getText().toString().isEmpty()){
            phoneEditText.setError(getResources().getString(R.string.please_enter_phone));
            return false;
        }
        Pattern pattern = Pattern.compile("[+]380\\d{9}");
        Matcher matcher = pattern.matcher(phoneEditText.getText().toString());
        if(!matcher.matches()){
            phoneEditText.setError(getResources().getString(R.string.driver_telephone_invalid));
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
                currentPrice = 0;
                carClass = null;
                standardRadioButton.setSelected(false);
                standardRadioButton.setChecked(false);
                saleRadioButton.setSelected(false);
                saleRadioButton.setChecked(false);
            } else {
                if(saleRadioButton.isSelected()) {
                    currentPrice = (priceStandard - (priceStandard * (userRank.getSaleValue() / 100)));
                }
                else{
                    currentPrice = priceStandard;
                }
                carClass = CAR_CLASSES.STANDARD;
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
                currentPrice = 0;
                carClass = null;
                comfortRadioButton.setSelected(false);
                comfortRadioButton.setChecked(false);
                saleRadioButton.setSelected(false);
                saleRadioButton.setChecked(false);
            }
            else{
                if(saleRadioButton.isSelected()) {
                    currentPrice = (priceComfort - (priceComfort * (userRank.getSaleValue() / 100)));
                }
                else{
                    currentPrice = priceComfort;
                }
                carClass = CAR_CLASSES.COMFORT;
                comfortRadioButton.setSelected(true);
                comfortRadioButton.setChecked(true);
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
                currentPrice = 0;
                carClass = null;
                eliteRadioButton.setSelected(false);
                eliteRadioButton.setChecked(false);
                saleRadioButton.setSelected(false);
                saleRadioButton.setChecked(false);
            }
            else{
                if(saleRadioButton.isSelected()) {
                    currentPrice = (priceElite - (priceElite * (userRank.getSaleValue() / 100)));
                }
                else{
                    currentPrice = priceElite;
                }
                carClass = CAR_CLASSES.ELITE;
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
                String militaryBonus = "";
                if(priceByClassResponse.isMilitaryBonus()){
                    militaryBonus = " with military bonus";
                }
                if(standardRadioButton.isSelected()){
                    currentPrice = priceStandard;
                }
                if(comfortRadioButton.isSelected()){
                    currentPrice = priceComfort;
                }
                if(eliteRadioButton.isSelected()){
                    currentPrice = priceElite;
                }
                String str1 = "PRICE: " + priceStandard + militaryBonus;
                String str2 = "PRICE: " + priceComfort + militaryBonus;
                String str3 = "PRICE: " + priceElite + militaryBonus;
                priceStandardTextView.setText(str1);
                priceComfortTextView.setText(str2);
                priceEliteTextView.setText(str3);
                saleRadioButton.setSelected(false);
                saleRadioButton.setChecked(false);
            }
            else{
                saleRadioButton.setSelected(true);
                saleRadioButton.setChecked(true);
                String militaryBonus = "";
                if(priceByClassResponse.isMilitaryBonus()){
                    militaryBonus = " with military bonus";
                }
                String str1 = "PRICE: " + (priceStandard - (priceStandard * (userRank.getSaleValue() / 100))) + militaryBonus;
                String str2 = "PRICE: " + (priceComfort - (priceComfort * (userRank.getSaleValue() / 100))) + militaryBonus;
                String str3 = "PRICE: " + (priceElite - (priceElite * (userRank.getSaleValue() / 100))) + militaryBonus;
                priceStandardTextView.setText(str1);
                priceComfortTextView.setText(str2);
                priceEliteTextView.setText(str3);
                if(standardRadioButton.isSelected()){
                    currentPrice = (priceStandard - (priceStandard * (userRank.getSaleValue() / 100)));
                }
                if(comfortRadioButton.isSelected()){
                    currentPrice = (priceComfort - (priceComfort * (userRank.getSaleValue() / 100)));
                }
                if(eliteRadioButton.isSelected()){
                    currentPrice = (priceElite - (priceElite * (userRank.getSaleValue() / 100)));
                }
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
                currentPrice = 0;
                carClass = null;
                standardFreeOrdersRadioButton.setSelected(false);
                standardFreeOrdersRadioButton.setChecked(false);
            }
            else{
                String militaryBonus = "";
                if(priceByClassResponse.isMilitaryBonus()){
                    militaryBonus = " with military bonus";
                }
                String str1 = "PRICE: " + priceStandard + militaryBonus;
                String str2 = "PRICE: " + priceComfort + militaryBonus;
                String str3 = "PRICE: " + priceElite + militaryBonus;
                priceStandardTextView.setText(str1);
                priceComfortTextView.setText(str2);
                priceEliteTextView.setText(str3);
                currentPrice = 0;
                carClass = CAR_CLASSES.STANDARD;
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
                currentPrice = 0;
                carClass = null;
                comfortFreeOrdersRadioButton.setSelected(false);
                comfortFreeOrdersRadioButton.setChecked(false);
            }
            else{
                String militaryBonus = "";
                if(priceByClassResponse.isMilitaryBonus()){
                    militaryBonus = " with military bonus";
                }
                String str1 = "PRICE: " + priceStandard + militaryBonus;
                String str2 = "PRICE: " + priceComfort + militaryBonus;
                String str3 = "PRICE: " + priceElite + militaryBonus;
                priceStandardTextView.setText(str1);
                priceComfortTextView.setText(str2);
                priceEliteTextView.setText(str3);
                carClass = CAR_CLASSES.COMFORT;
                currentPrice = 0;
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
                carClass = null;
                eliteFreeOrdersRadioButton.setSelected(false);
                eliteFreeOrdersRadioButton.setChecked(false);
            }
            else{
                String militaryBonus = "";
                if(priceByClassResponse.isMilitaryBonus()){
                    militaryBonus = " with military bonus";
                }
                String str1 = "PRICE: " + priceStandard + militaryBonus;
                String str2 = "PRICE: " + priceComfort + militaryBonus;
                String str3 = "PRICE: " + priceElite + militaryBonus;
                priceStandardTextView.setText(str1);
                priceComfortTextView.setText(str2);
                priceEliteTextView.setText(str3);
                carClass = CAR_CLASSES.ELITE;
                currentPrice = 0;
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
        rankInfoApi.getRankInfo("Bearer " + UserInfoService.getProperty("access_token")).enqueue(new Callback<List<Rank>>() {
            @Override
            public void onResponse(Call<List<Rank>> call, Response<List<Rank>> response) {
                if(response.body()!=null) {
                    try {
                        if(response.errorBody() != null){
                            if(response.errorBody().string().contains(getResources()
                                    .getString(R.string.token_expired))){
                                refreshToken();
                            }
                        }
                        else{
                            getRanksInfo(response.body());
                            getRanksStats();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Rank>> call, Throwable t) {
                MakeOrderActivity.this.runOnUiThread(() ->
                        Toast.makeText(MakeOrderActivity.this,
                                "Failed to load ranks info",
                        Toast.LENGTH_SHORT).show());

            }
        });
    }

    private void getRanksInfo(List<Rank> rankList){
        for(Rank rank: rankList){
            if(rank.getRankId() == Integer.parseInt(UserInfoService.getProperty("rankId"))){
                this.userRank = rank;
                break;
            }
        }
    }

    private void getRanksStats(){
        if(Integer.parseInt(UserInfoService.getProperty("rankId")) > 1){
            getBaseRankUserStatsRequest();
            if(Integer.parseInt(UserInfoService.getProperty("rankId")) > 4){
                if(favouriteDriverId > 0){
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
        rankInfoApi.getUserEliteRankAchievementsInfoByDriver("Bearer " + UserInfoService.getProperty("access_token"),
                        Integer.parseInt(UserInfoService.getProperty("userId")),
                        Integer.parseInt(UserInfoService.getProperty("rankId")), favouriteDriverId)
                .enqueue(new Callback<List<UserEliteRankAchievementInfo>>() {

                    @Override
                    public void onResponse(Call<List<UserEliteRankAchievementInfo>> call,
                                           Response<List<UserEliteRankAchievementInfo>> response) {
                        if(response.body()!=null) {
                            try {
                                if(response.errorBody() != null){
                                    if(response.errorBody().string().contains(getResources()
                                            .getString(R.string.token_expired))){
                                        refreshToken();
                                    }
                                }
                                else{
                                    setEliteRanksStats(response.body());
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<UserEliteRankAchievementInfo>> call, Throwable t) {
                        MakeOrderActivity.this.runOnUiThread(() ->
                                Toast.makeText(MakeOrderActivity.this,
                                getResources().getString(R.string.error_to_get_base_rank_user_info),
                                Toast.LENGTH_SHORT).show());
                    }
                });
    }

    private void getBaseRankUserStatsRequest(){
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi rankInfoApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        rankInfoApi.getUserRankAchievementsInfo("Bearer " + UserInfoService.getProperty("access_token"),
                        Integer.parseInt(UserInfoService.getProperty("userId")),
                        Integer.parseInt(UserInfoService.getProperty("rankId")))
                .enqueue(new Callback<UserRankAchievementInfo>() {

                    @Override
                    public void onResponse(Call<UserRankAchievementInfo> call, Response<UserRankAchievementInfo> response) {
                        if(response.body()!=null){
                            try {
                                if(response.errorBody() != null){
                                    if(response.errorBody().string().contains(getResources()
                                            .getString(R.string.token_expired))){
                                        refreshToken();
                                    }
                                }
                                else{
                                    setBaseRankStats(response.body());
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserRankAchievementInfo> call, Throwable t) {
                        MakeOrderActivity.this.runOnUiThread(() -> Toast.makeText(MakeOrderActivity.this,
                                getResources().getString(R.string.error_to_get_base_rank_user_info),
                                Toast.LENGTH_SHORT).show());

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
        rankInfoApi.getUserEliteRankAchievementsInfo("Bearer " + UserInfoService.getProperty("access_token"),
                        Integer.parseInt(UserInfoService.getProperty("userId")),
                        Integer.parseInt(UserInfoService.getProperty("rankId")))
                .enqueue(new Callback<List<UserEliteRankAchievementInfo>>() {

                    @Override
                    public void onResponse(Call<List<UserEliteRankAchievementInfo>> call,
                                           Response<List<UserEliteRankAchievementInfo>> response) {
                        Log.d("ElRk", response.body().toString());
                        if(response.body()!=null){
                            try {
                                if(response.errorBody() != null){
                                    if(response.errorBody().string().contains(getResources()
                                            .getString(R.string.token_expired))){
                                        refreshToken();
                                    }
                                }
                                else{
                                    setEliteRanksStats(response.body());
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<UserEliteRankAchievementInfo>> call, Throwable t) {
                        MakeOrderActivity.this.runOnUiThread(() -> Toast.makeText(MakeOrderActivity.this,
                                getResources().getString(R.string.error_to_get_base_rank_user_info),
                                Toast.LENGTH_SHORT).show());

                    }
                });
    }

    private void setEliteRanksStats(List<UserEliteRankAchievementInfo> userEliteRankAchievementInfoList){
        System.out.println(userEliteRankAchievementInfoList);
        for(UserEliteRankAchievementInfo userEliteRankAchievementInfo: userEliteRankAchievementInfoList){
            checkUserNumberOfOrdersOnNull(userEliteRankAchievementInfo);
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
        if(userEliteRankAchievementInfo.getCarClassId() == 3
                && userEliteRankAchievementInfo.getNumberOfUsesFreeOrder() != 0){
            freeOrderBonusesTextView.setVisibility(View.VISIBLE);
            eliteBonusesCardView.setVisibility(View.VISIBLE);
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
        PriceByClassRequest priceByClassRequest = new PriceByClassRequest(
                Integer.parseInt(UserInfoService.getProperty("userId")), userAddressFrom, userAddressTo, distance);
        rankInfoApi.getUserOrderPriceByClass("Bearer " + UserInfoService.getProperty("access_token"),
                priceByClassRequest).enqueue(new Callback<PriceByClassResponse>() {

                    @Override
                    public void onResponse(Call<PriceByClassResponse> call, Response<PriceByClassResponse> response) {
                        if(response.body()!=null){
                            try {
                                if(response.errorBody() != null){
                                    if(response.errorBody().string().contains(getResources()
                                            .getString(R.string.token_expired))){
                                        refreshToken();
                                    }
                                }
                                else{
                                    setTextPrice(response.body());
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PriceByClassResponse> call, Throwable t) {
                        MakeOrderActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(MakeOrderActivity.this,
                                        getResources().getString(R.string.error_to_get_user_order_price_by_class),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    private void showWaitForDriverResponse() {
        dialogBuilder = new AlertDialog.Builder(MakeOrderActivity.this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.user_wait_response, null);
        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.card_view_border_reverse, getTheme()));
        dialog.show();
    }

    public void subscribeUser(int userID) throws ExecutionException, InterruptedException {

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
                        allow = false;
                        showRejectDriverResponse();
                        noAnswerDrivers.add(currentDriverId);
                    }
                    isGetAnswer = true;
                    if(stompSession!=null && stompSession.isConnected()) {
                        new Thread(() -> stompSession.disconnect()).start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setTextPrice(PriceByClassResponse response) {
        priceByClassResponse = response;
        runOnUiThread(() -> {
            try {
                System.out.println(response.getPriceByClass().get(0));
                String militaryBonus = "";
                if(response.isMilitaryBonus()){
                    militaryBonus = " with military bonus";
                }
                priceStandard = response.getPriceByClass().get(0);
                String str1 = "PRICE: " + priceStandard + militaryBonus;
                priceComfort = response.getPriceByClass().get(1);
                String str2 = "PRICE: " + priceComfort + militaryBonus;
                priceElite = response.getPriceByClass().get(2);
                String str3 = "PRICE: " + priceElite + militaryBonus;
                priceStandardTextView.setText(str1);
                priceComfortTextView.setText(str2);
                priceEliteTextView.setText(str3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void showAcceptDriverResponse() {
        runOnUiThread(() -> {
            dialog.cancel();
            dialogBuilder = new AlertDialog.Builder(MakeOrderActivity.this);
            final View contactPopupView = getLayoutInflater().inflate(R.layout.user_accept_response, null);
            dialogBuilder.setView(contactPopupView);
            dialog = dialogBuilder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.card_view_border_reverse, getTheme()));
            dialog.show();
            System.out.println(selectedDriver);
            String fullName = selectedDriver.getDriverFirstName() + " " + selectedDriver.getDriverSurName();
            String carName = selectedDriver.getCarProducer() + " " + selectedDriver.getCarBrand();
            Intent intent = new Intent(MakeOrderActivity.this, SetRatingActivity.class);
            intent.putExtra("price", String.valueOf(currentPrice));
            intent.putExtra("isUseSale",  String.valueOf(saleRadioButton.isSelected()));
            intent.putExtra("carClass", carClass.name());
            intent.putExtra("distance", String.valueOf(distance));
            intent.putExtra("isFavouriteDriver",  String.valueOf(isFavouriteDriver));
            intent.putExtra("rankId",  String.valueOf(userRank.getRankId()));
            intent.putExtra("currentDriverId",  String.valueOf(currentDriverId));
            intent.putExtra("driverFullName",  fullName);
            intent.putExtra("driverCar",  carName);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                dialog.dismiss();
                startActivity(intent);
            }, 5000);
        });
    }

    private void showRejectDriverResponse() {
        runOnUiThread(() -> {
            dialog.cancel();
            dialogBuilder = new AlertDialog.Builder(MakeOrderActivity.this);
            final View contactPopupView = getLayoutInflater().inflate(R.layout.user_reject_response, null);
            dialogBuilder.setView(contactPopupView);
            dialog = dialogBuilder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.card_view_border_reverse, getTheme()));
            dialog.show();
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                dialog.dismiss();
            }, 5000);
        });

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
        if(stompSession!=null && stompSession.isConnected()) {
            new Thread(() -> stompSession.disconnect()).start();
        }
        startActivity(intent);
    }


    private void refreshToken() {
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi api = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        api.refreshToken("Bearer " + UserInfoService.getProperty("refresh_token"))
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
                                UserInfoService.addProperty("access_token", registerResponse.getAccessToken());
                                UserInfoService.addProperty("refresh_token", registerResponse.getRefreshToken());
                                if(stompSession!=null && stompSession.isConnected()) {
                                    new Thread(() -> stompSession.disconnect()).start();
                                }
                                finish();
                                startActivity(getIntent());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        MakeOrderActivity.this.runOnUiThread(() -> Toast.makeText(MakeOrderActivity.this,
                                getResources().getString(R.string.distance_is_to_short),
                                Toast.LENGTH_SHORT).show());
                    }
                });
    }

    private void goLogin() {
        Intent intent = new Intent(this, UserLoginActivity.class);
        if(stompSession!=null && stompSession.isConnected()) {
            new Thread(() -> stompSession.disconnect()).start();
        }
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(!allow) {
            super.onBackPressed();
        }
    }
}
