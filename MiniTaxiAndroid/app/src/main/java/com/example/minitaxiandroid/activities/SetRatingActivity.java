package com.example.minitaxiandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.minitaxiandroid.R;
import com.example.minitaxiandroid.api.MiniTaxiApi;
import com.example.minitaxiandroid.api.RetrofitService;
import com.example.minitaxiandroid.entities.auth.RegisterResponse;
import com.example.minitaxiandroid.entities.document.CAR_CLASSES;
import com.example.minitaxiandroid.entities.messages.MyMessage;
import com.example.minitaxiandroid.entities.messages.SendOrder;
import com.example.minitaxiandroid.entities.messages.UserSendDate;
import com.example.minitaxiandroid.entities.userinfo.FavouriteDriver;
import com.example.minitaxiandroid.entities.userinfo.FavouriteDriverUserInfo;
import com.example.minitaxiandroid.services.UserInfoService;
import com.example.minitaxiandroid.websocket.WebSocketClient;
import com.like.LikeButton;
import com.like.OnLikeListener;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.concurrent.ListenableFuture;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.minitaxiandroid.services.ObjectParserService.parseUserSendDateFromString;


public class SetRatingActivity extends AppCompatActivity {

    private RatingBar ratingDriverBar;
    private Button evaluate;
    private LikeButton likeDriverButton;
    private UserSendDate userSendDate;
    private StompSession stompSession;
    private String userId, price, distance, rankId, driverId;
    private EditText userComment;
    private boolean isUseSale;
    private int isSendOrder = 0;
    private CAR_CLASSES carClass;
    private List<FavouriteDriverUserInfo> favouriteDriverUserInfoList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_rating);
        ratingDriverBar = findViewById(R.id.ratingDriverBar);
        likeDriverButton = findViewById(R.id.driverLikeButton);
        userComment = findViewById(R.id.userCommentText);
        ratingDriverBar.setEnabled(false);
        UserInfoService.init(SetRatingActivity.this);
        likeDriverButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                likeDriver();
                likeButton.setEnabled(false);
            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });
        likeDriverButton.setEnabled(false);
        evaluate = findViewById(R.id.evaluateRatingButton);
        evaluate.setOnClickListener(view -> {
            sendOrder();
        });
        boolean isFavouriteDriver = Boolean.parseBoolean(getDate(savedInstanceState, "isFavouriteDriver"));
        if(isFavouriteDriver){
            likeDriverButton.setLiked(true);
            likeDriverButton.setEnabled(false);
        }
        else{
            driverId = getDate(savedInstanceState, "currentDriverId");
            getFavouritesDrivers();
        }
        new Thread(() -> {
            WebSocketClient userClient = new WebSocketClient();
            ListenableFuture<StompSession> f = userClient.connect();
            userId = UserInfoService.getProperty("userId");
            price = getDate(savedInstanceState, "price");
            distance = getDate(savedInstanceState, "distance");
            isUseSale = Boolean.parseBoolean(getDate(savedInstanceState, "isUseSale"));
            carClass = CAR_CLASSES.valueOf(getDate(savedInstanceState, "carClass"));
            rankId = getDate(savedInstanceState, "rankId");
            try {
                stompSession = f.get();
                subscribe(Integer.parseInt(userId));
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void likeDriver() {
        isSendOrder = 1;
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi api = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        api.addFavouriteDriver("Bearer " + UserInfoService.getProperty("access_token"),
                new FavouriteDriver(0, userSendDate.getDriverId(),
                        Integer.parseInt(userId)))
                .enqueue(new Callback<MyMessage>() {
            @Override
            public void onResponse(Call<MyMessage> call, Response<MyMessage> response) {
                if(response.body()!=null){
                    try {
                        if(response.errorBody() != null){
                            if(response.errorBody().string().contains(getResources()
                                    .getString(R.string.token_expired))){
                                refreshToken();
                            }
                        }
                        else{
                            Toast.makeText(SetRatingActivity.this,
                                    getResources().getString(R.string.driver_successfully_liked),
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyMessage> call, Throwable t) {
                SetRatingActivity.this.runOnUiThread(() -> Toast.makeText(SetRatingActivity.this,
                        getResources().getString(R.string.error_to_like_driver),
                        Toast.LENGTH_SHORT).show());

            }
        });
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
                                if(isSendOrder == 1){
                                    likeDriver();
                                }
                                else if(isSendOrder == 2){
                                    if(stompSession!=null && stompSession.isConnected()) {
                                        new Thread(() -> {
                                            sendOrder();
                                            stompSession.disconnect();
                                        }).start();
                                    }
                                }
                                else if(isSendOrder == 3){
                                    getFavouritesDrivers();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        SetRatingActivity.this.runOnUiThread(() -> Toast.makeText(SetRatingActivity.this, "Failed to check user authentication",
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

    public void subscribe(int userID) throws ExecutionException, InterruptedException {
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
    }

    private void setEnabledButtons() {
        runOnUiThread(() -> {
            evaluate.setEnabled(true);
            ratingDriverBar.setEnabled(true);
            likeDriverButton.setEnabled(true);
        });
    }

    private void goMain() {
        Intent intent = new Intent(SetRatingActivity.this, MainActivity.class);
        if(stompSession!=null && stompSession.isConnected()) {
            new Thread(() -> stompSession.disconnect()).start();
        }
        startActivity(intent);
    }

    public void getFavouritesDrivers(){
        isSendOrder = 3;
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi favouriteDriversApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        String userId = UserInfoService.getProperty("userId");
        favouriteDriversApi.getFavouriteDriverUserInfo("Bearer " + UserInfoService.getProperty("access_token"),
                        Integer.valueOf(userId))
                .enqueue(new Callback<List<FavouriteDriverUserInfo>>() {
                    @Override
                    public void onResponse(Call<List<FavouriteDriverUserInfo>> call, Response<List<FavouriteDriverUserInfo>> response) {
                        if(response.body()!=null){
                            try {
                                if(response.errorBody() != null){
                                    if(response.errorBody().string().contains(getResources()
                                            .getString(R.string.token_expired))){
                                        refreshToken();
                                    }
                                }
                                else{
                                    favouriteDriverUserInfoList = response.body();
                                    System.out.println(favouriteDriverUserInfoList);
                                    if(checkIfFavourite()){
                                        runOnUiThread(() -> {
                                            likeDriverButton.setLiked(true);
                                            likeDriverButton.setEnabled(false);
                                        });
                                    }
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<FavouriteDriverUserInfo>> call, Throwable t) {
                        SetRatingActivity.this.runOnUiThread(() -> Toast.makeText(SetRatingActivity.this,
                                "Failed to load favourite drivers info",
                                Toast.LENGTH_SHORT).show());
                    }
                });
    }

    private boolean checkIfFavourite() {
        for(FavouriteDriverUserInfo favouriteDriverUserInfo: favouriteDriverUserInfoList){
            if(favouriteDriverUserInfo.getDriverId() == Integer.parseInt(driverId)){
                return true;
            }
        }
        return false;
    }

    private void sendOrder() {
        isSendOrder = 2;
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi api = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        SendOrder sendOrder = new SendOrder(userSendDate.getDriverId(),
                Integer.parseInt(userId), userSendDate.getCustomerName(),
                userSendDate.getAddressCustomer(), userSendDate.getAddressDelivery(),
                userSendDate.getTelephoneNumber(), userComment.getText().toString(),
                Float.parseFloat(price), isUseSale, carClass,
                Float.parseFloat(distance), ratingDriverBar.getRating(), Integer.parseInt(rankId));
        api.completeOrder("Bearer " + UserInfoService.getProperty("access_token"),
                sendOrder).enqueue(new Callback<MyMessage>() {

            @Override
            public void onResponse(Call<MyMessage> call, Response<MyMessage> response) {
                if(response.body()!=null){
                    try {
                        if(response.errorBody() != null){
                            if(response.errorBody().string().contains(getResources()
                                    .getString(R.string.token_expired))){
                                refreshToken();
                            }
                        }
                        else{
                            SetRatingActivity.this.runOnUiThread(() ->
                                    Toast.makeText(SetRatingActivity.this,
                                    getResources().getString(R.string.successfully_add_order),
                                    Toast.LENGTH_SHORT).show());
                            goMain();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyMessage> call, Throwable t) {
                System.out.println("t: " + t.getMessage());
                SetRatingActivity.this.runOnUiThread(() ->
                        Toast.makeText(SetRatingActivity.this,
                        getResources().getString(R.string.error_to_add_order),
                        Toast.LENGTH_SHORT).show());
            }
        });
    }
}
