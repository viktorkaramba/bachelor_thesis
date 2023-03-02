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
import com.example.minitaxiandroid.adapter.FavouriteAddressesAdapter;
import com.example.minitaxiandroid.entities.messages.OrderInfo;
import com.example.minitaxiandroid.entities.messages.UserPickCar;
import com.example.minitaxiandroid.entities.userinfo.FavouriteAddressesUserInfo;
import com.example.minitaxiandroid.entities.userinfo.FavouriteDriverUserInfo;
import com.example.minitaxiandroid.entities.userinfo.UserOrderInfo;
import com.example.minitaxiandroid.retrofit.MiniTaxiApi;
import com.example.minitaxiandroid.retrofit.RetrofitService;
import com.example.minitaxiandroid.retrofit.SelectListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        favouriteAddressBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goHome();
            }
        });
        getFavouritesAddresses();
    }


    private void goHome(){
        Intent intent = new Intent(FavouriteAddressesActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void getFavouritesAddresses(){
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi favouriteAddressApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        String userId = "1";
        favouriteAddressApi.getFavouriteAddressesUserInfo(userId)
                .enqueue(new Callback<List<FavouriteAddressesUserInfo>>() {
                    @Override
                    public void onResponse(Call<List<FavouriteAddressesUserInfo>> call, Response<List<FavouriteAddressesUserInfo>> response) {
                        populateListView(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<FavouriteAddressesUserInfo>> call, Throwable t) {
                        Toast.makeText(FavouriteAddressesActivity.this, "Failed to load favourite drivers info",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void populateListView(List<FavouriteAddressesUserInfo> body) {
        FavouriteAddressesAdapter favouriteAddressesAdapter = new FavouriteAddressesAdapter(body, this);
        recyclerView.setAdapter(favouriteAddressesAdapter);
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