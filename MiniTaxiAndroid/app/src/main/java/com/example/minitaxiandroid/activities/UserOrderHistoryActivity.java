package com.example.minitaxiandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.minitaxiandroid.R;
import com.example.minitaxiandroid.adapter.UserOrderHistoryAdapter;
import com.example.minitaxiandroid.entities.messages.OrderInfo;
import com.example.minitaxiandroid.entities.messages.UserPickCar;
import com.example.minitaxiandroid.entities.userinfo.FavouriteAddressesUserInfo;
import com.example.minitaxiandroid.entities.userinfo.FavouriteDriverUserInfo;
import com.example.minitaxiandroid.entities.userinfo.UserOrderInfo;
import com.example.minitaxiandroid.api.MiniTaxiApi;
import com.example.minitaxiandroid.api.RetrofitService;
import com.example.minitaxiandroid.api.SelectListener;
import com.example.minitaxiandroid.services.UserLoginInfoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;


public class UserOrderHistoryActivity extends AppCompatActivity implements SelectListener {
    private RecyclerView recyclerView;
    private Button backButton;
    private String userId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_history);
        recyclerView = findViewById(R.id.userOrderHistoryList);
        backButton = findViewById(R.id.userOrderHistoryBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goHome();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        UserLoginInfoService.init(UserOrderHistoryActivity.this);
        getUserOrders();
    }

    public void goHome(){
        Intent intent = new Intent(UserOrderHistoryActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void getUserOrders(){
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi userOrderHistoryApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        userOrderHistoryApi.getUserOrderHistory(UserLoginInfoService.getProperty("userId"))
                .enqueue(new Callback<List<UserOrderInfo>>() {
                    @Override
                    public void onResponse(Call<List<UserOrderInfo>> call, Response<List<UserOrderInfo>> response) {
                        populateListView(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<UserOrderInfo>> call, Throwable t) {
                        Toast.makeText(UserOrderHistoryActivity.this, "Failed to load user orders",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void populateListView(List<UserOrderInfo> body) {
        UserOrderHistoryAdapter userOrderHistoryAdapter = new UserOrderHistoryAdapter(body, this);
        recyclerView.setAdapter(userOrderHistoryAdapter);
    }

    public void goMakeOrder(UserPickCar userPickCar){
        Intent intent = new Intent(this, MakeOrderActivity.class);
        intent.putExtra("driverId", String.valueOf(userPickCar.getDriverId()));
        intent.putExtra("userId", userId);
        intent.putExtra("price", String.valueOf(userPickCar.getPrice()));
        startActivity(intent);
    }



    @Override
    public void onItemClicked(UserPickCar userPickCar) {
        goMakeOrder(userPickCar);
    }

    @Override
    public void onItemClicked(OrderInfo orderInfo) {

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
}
