package com.example.energytaxiandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.energytaxiandroid.R;
import com.example.energytaxiandroid.adapter.FavouriteAddressesAdapter;
import com.example.energytaxiandroid.api.MiniTaxiApi;
import com.example.energytaxiandroid.api.RetrofitService;
import com.example.energytaxiandroid.api.SelectListener;
import com.example.energytaxiandroid.entities.auth.RegisterResponse;
import com.example.energytaxiandroid.entities.messages.OrderInfo;
import com.example.energytaxiandroid.entities.userinfo.FavouriteAddressesUserInfo;
import com.example.energytaxiandroid.entities.userinfo.FavouriteDriverUserInfo;
import com.example.energytaxiandroid.entities.userinfo.UserOrderInfo;
import com.example.energytaxiandroid.services.UserInfoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class FavouriteAddressesActivity extends AppCompatActivity implements SelectListener {

    private RecyclerView recyclerView;
    private Button favouriteAddressBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_addresses);
        recyclerView = findViewById(R.id.favouriteAddressesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        favouriteAddressBackButton = findViewById(R.id.favouriteAddressBackButton);
        favouriteAddressBackButton.setOnClickListener(view -> goHome());
        UserInfoService.init(FavouriteAddressesActivity.this);
        getFavouritesAddresses();
    }


    private void goHome(){
        Intent intent = new Intent(FavouriteAddressesActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void getFavouritesAddresses(){
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi favouriteAddressApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        favouriteAddressApi.getFavouriteAddressesUserInfo("Bearer " + UserInfoService.getProperty("access_token"),
                        Integer.valueOf(UserInfoService.getProperty("userId")))
                .enqueue(new Callback<List<FavouriteAddressesUserInfo>>() {
                    @Override
                    public void onResponse(Call<List<FavouriteAddressesUserInfo>> call, Response<List<FavouriteAddressesUserInfo>> response) {
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
                    public void onFailure(Call<List<FavouriteAddressesUserInfo>> call, Throwable t) {
                        FavouriteAddressesActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(FavouriteAddressesActivity.this, "Failed to load favourite drivers info",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    private void populateListView(List<FavouriteAddressesUserInfo> body) {
        FavouriteAddressesAdapter favouriteAddressesAdapter = new FavouriteAddressesAdapter(body,
                this, this);
        recyclerView.setAdapter(favouriteAddressesAdapter);
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
                        FavouriteAddressesActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(FavouriteAddressesActivity.this, "Failed to check user authentication",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
    }

    private void goLogin() {
        Intent intent = new Intent(this, UserLoginActivity.class);
        startActivity(intent);
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