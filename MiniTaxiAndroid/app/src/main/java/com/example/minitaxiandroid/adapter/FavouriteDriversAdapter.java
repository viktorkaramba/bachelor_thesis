package com.example.minitaxiandroid.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.minitaxiandroid.R;
import com.example.minitaxiandroid.activities.FavouriteDriversActivity;
import com.example.minitaxiandroid.activities.UserLoginActivity;
import com.example.minitaxiandroid.api.MiniTaxiApi;
import com.example.minitaxiandroid.api.RetrofitService;
import com.example.minitaxiandroid.api.SelectListener;
import com.example.minitaxiandroid.entities.auth.RegisterResponse;
import com.example.minitaxiandroid.entities.messages.MyMessage;
import com.example.minitaxiandroid.entities.userinfo.FavouriteDriverUserInfo;
import com.example.minitaxiandroid.services.UserInfoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class FavouriteDriversAdapter extends RecyclerView.Adapter<FavouriteDriversHolder>{

    private List<FavouriteDriverUserInfo> favouriteDriverUserInfoList;
    private SelectListener selectListener;
    private FavouriteDriversActivity favouriteDriversActivity;

    public FavouriteDriversAdapter(List<FavouriteDriverUserInfo> favouriteDriverUserInfoList, SelectListener selectListener,
                                   FavouriteDriversActivity favouriteDriversActivity) {
        this.favouriteDriverUserInfoList = favouriteDriverUserInfoList;
        this.selectListener = selectListener;
        this.favouriteDriversActivity = favouriteDriversActivity;
    }

    @NonNull
    @Override
    public FavouriteDriversHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.favourite_drivers_item, viewGroup, false);
        return new FavouriteDriversHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteDriversHolder favouriteDriversHolder, @SuppressLint("RecyclerView") int i) {
        FavouriteDriverUserInfo favouriteDriverUserInfo = favouriteDriverUserInfoList.get(i);
        String driverFullName =
                favouriteDriverUserInfo.getDriverFirstName() + " " +
                        favouriteDriverUserInfo.getDriverSurName();
        favouriteDriversHolder.fullName.setText(driverFullName);
        String carName =
                favouriteDriverUserInfo.getCarProducer() + " " +
                        favouriteDriverUserInfo.getCarBrand();
        favouriteDriversHolder.carName.setText(carName);
        String countsOrder = String.valueOf(favouriteDriverUserInfo.getCountOrders());
        favouriteDriversHolder.countsOrder.setText(countsOrder);
        Float rating = favouriteDriverUserInfo.getAverageRating();
        DecimalFormat df = new DecimalFormat("#.#");
        String avgRating = String.valueOf(df.format(rating).replace(',','.'));
        favouriteDriversHolder.averageRating.setText(avgRating);
        favouriteDriversHolder.deleteFavouriteDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDriver(favouriteDriverUserInfo.getDriverId());
            }
        });
        favouriteDriversHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectListener.onItemClicked(favouriteDriverUserInfoList.get(i));
            }
        });
    }

    private void deleteDriver(int ID) {
        UserInfoService.init(favouriteDriversActivity);
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi favouriteDriversApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        Integer userId = Integer.valueOf(UserInfoService.getProperty("userId"));
        favouriteDriversApi.deleteFavouriteDriverUserInfo("Bearer " + UserInfoService.getProperty("access_token"),
                        ID, userId)
                .enqueue(new Callback<MyMessage>() {
                    @Override
                    public void onResponse(Call<MyMessage> call, Response<MyMessage> response) {
                        if(response.body()!=null){
                            try {
                                if(response.errorBody() != null){
                                    if(response.errorBody().string().contains(favouriteDriversActivity.getResources()
                                            .getString(R.string.token_expired))){
                                        refreshToken();
                                    }
                                }
                                else{
                                    if(response.body().getContent().equals("Successfully delete favourite driver")){
                                        favouriteDriversActivity.finish();
                                        favouriteDriversActivity.startActivity(favouriteDriversActivity.getIntent());
                                        Toast.makeText(favouriteDriversActivity,
                                                "Successfully delete favourite driver",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(favouriteDriversActivity,
                                                "Error delete favourite driver",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MyMessage> call, Throwable t) {
                        favouriteDriversActivity.runOnUiThread(() ->
                                Toast.makeText(favouriteDriversActivity, "Failed to delete favourite driver info",
                                Toast.LENGTH_SHORT).show());
                        Log.d("get favourite driver log", "Failed to delete favourite driver info");
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
                            if(registerResponse.getAccessToken().equals(favouriteDriversActivity.getResources()
                                    .getString(R.string.token_expired))){
                                goLogin();
                            }
                            else if(registerResponse.getAccessToken().equals(favouriteDriversActivity.getResources()
                                    .getString(R.string.username_not_found))){
                                goLogin();
                            }
                            else {
                                UserInfoService.addProperty("access_token", registerResponse.getAccessToken());
                                UserInfoService.addProperty("refresh_token", registerResponse.getRefreshToken());
                                favouriteDriversActivity.finish();
                                favouriteDriversActivity.startActivity(favouriteDriversActivity.getIntent());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        favouriteDriversActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(favouriteDriversActivity, "Failed to check user authentication",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }
    private void goLogin() {
        Intent intent = new Intent(favouriteDriversActivity, UserLoginActivity.class);
        favouriteDriversActivity.startActivity(intent);
    }

    @Override
    public int getItemCount() {
       return favouriteDriverUserInfoList.size();
    }
}
