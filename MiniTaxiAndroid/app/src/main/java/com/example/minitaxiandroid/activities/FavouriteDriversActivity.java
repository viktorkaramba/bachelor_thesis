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

public class FavouriteDriversActivity extends AppCompatActivity implements SelectListener {

    private RecyclerView recyclerView;
    private Button favouriteDriverBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_drivers);
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
        String userId = "1";
        favouriteDriversApi.getFavouriteDriverUserInfo(userId)
                .enqueue(new Callback<List<FavouriteDriverUserInfo>>() {
                    @Override
                    public void onResponse(Call<List<FavouriteDriverUserInfo>> call, Response<List<FavouriteDriverUserInfo>> response) {
                        populateListView(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<FavouriteDriverUserInfo>> call, Throwable t) {
                        Toast.makeText(FavouriteDriversActivity.this, "Failed to load favourite drivers info",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void populateListView(List<FavouriteDriverUserInfo> body) {
        FavouriteDriversAdapter favouriteDriversAdapter = new FavouriteDriversAdapter(body, this);
        recyclerView.setAdapter(favouriteDriversAdapter);
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