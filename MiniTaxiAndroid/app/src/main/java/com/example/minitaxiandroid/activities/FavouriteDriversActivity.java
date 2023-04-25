package com.example.minitaxiandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.minitaxiandroid.R;
import com.example.minitaxiandroid.adapter.FavouriteDriversAdapter;
import com.example.minitaxiandroid.api.MiniTaxiApi;
import com.example.minitaxiandroid.api.RetrofitService;
import com.example.minitaxiandroid.api.SelectListener;
import com.example.minitaxiandroid.entities.auth.RegisterResponse;
import com.example.minitaxiandroid.entities.messages.OrderInfo;
import com.example.minitaxiandroid.entities.messages.UserPickCar;
import com.example.minitaxiandroid.entities.userinfo.FavouriteAddressesUserInfo;
import com.example.minitaxiandroid.entities.userinfo.FavouriteDriverUserInfo;
import com.example.minitaxiandroid.entities.userinfo.UserOrderInfo;
import com.example.minitaxiandroid.services.UserInfoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class FavouriteDriversActivity extends AppCompatActivity implements SelectListener {

    private RecyclerView recyclerView;
    private Button favouriteDriverBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_drivers);
        UserInfoService.init(FavouriteDriversActivity.this);
        recyclerView = findViewById(R.id.favouriteDriversList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        favouriteDriverBackButton = findViewById(R.id.favouriteDriverBackButton);
        favouriteDriverBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goHome();
            }
        });
        getFavouritesDrivers();
    }

    private void goHome(){
        Intent intent = new Intent(FavouriteDriversActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void getFavouritesDrivers(){
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
                                    populateListView(response.body());
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<FavouriteDriverUserInfo>> call, Throwable t) {
                        FavouriteDriversActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(FavouriteDriversActivity.this,
                                        "Failed to load favourite drivers info",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    private void populateListView(List<FavouriteDriverUserInfo> body) {
        FavouriteDriversAdapter favouriteDriversAdapter = new FavouriteDriversAdapter(body, this, this);
        recyclerView.setAdapter(favouriteDriversAdapter);
    }

    private void goLogin() {
        Intent intent = new Intent(this, UserLoginActivity.class);
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
                                finish();
                                startActivity(getIntent());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        FavouriteDriversActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(FavouriteDriversActivity.this, "Failed to check user authentication",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }
    @Override
    public void onItemClicked(UserPickCar userPickCar) {

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