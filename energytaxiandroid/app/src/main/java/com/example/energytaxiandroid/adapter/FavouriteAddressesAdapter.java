package com.example.energytaxiandroid.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.energytaxiandroid.R;
import com.example.energytaxiandroid.activities.FavouriteAddressesActivity;
import com.example.energytaxiandroid.activities.UserLoginActivity;
import com.example.energytaxiandroid.api.MiniTaxiApi;
import com.example.energytaxiandroid.api.RetrofitService;
import com.example.energytaxiandroid.api.SelectListener;
import com.example.energytaxiandroid.entities.auth.RegisterResponse;
import com.example.energytaxiandroid.entities.messages.MyMessage;
import com.example.energytaxiandroid.entities.userinfo.FavouriteAddressesUserInfo;
import com.example.energytaxiandroid.services.UserInfoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class FavouriteAddressesAdapter extends RecyclerView.Adapter<FavouriteAddressesHolder>{

    private List<FavouriteAddressesUserInfo> favouriteAddressesUserInfoList;
    private SelectListener selectListener;
    private FavouriteAddressesActivity favouriteAddressesActivity;

    public FavouriteAddressesAdapter(List<FavouriteAddressesUserInfo> favouriteAddressesUserInfoList,
                                     SelectListener selectListener,
                                     FavouriteAddressesActivity favouriteAddressesActivity) {
        this.favouriteAddressesUserInfoList = favouriteAddressesUserInfoList;
        this.selectListener = selectListener;
        this.favouriteAddressesActivity = favouriteAddressesActivity;
    }

    @NonNull
    @Override
    public FavouriteAddressesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.favourite_address_item, viewGroup, false);
        return new FavouriteAddressesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteAddressesHolder favouriteAddressesHolder,
                                 @SuppressLint("RecyclerView") int i) {
        FavouriteAddressesUserInfo favouriteAddressesUserInfo = favouriteAddressesUserInfoList.get(i);
        String name = favouriteAddressesUserInfo.getAddress();
        favouriteAddressesHolder.addressName.setText(name);
        String ordersCount = String.valueOf(favouriteAddressesUserInfo.getCount());
        favouriteAddressesHolder.countsOrders.setText(ordersCount);
        favouriteAddressesHolder.deleteFavouriteAddressButton.setOnClickListener(view -> deleteAddress(favouriteAddressesUserInfo.getAddress()));
        favouriteAddressesHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectListener.onItemClicked(favouriteAddressesUserInfoList.get(i));
            }
        });
    }

    private void deleteAddress(String address) {
        UserInfoService.init(favouriteAddressesActivity);
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi favouriteAddressApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        String userId = UserInfoService.getProperty("userId");
        favouriteAddressApi.deleteFavouriteAddressesUserInfo("Bearer " + UserInfoService.getProperty("access_token"),
                        Integer.valueOf(userId), address)
                .enqueue(new Callback<MyMessage>() {
                    @Override
                    public void onResponse(Call<MyMessage> call, Response<MyMessage> response) {
                        if(response.body()!=null){
                            try {
                                if(response.errorBody() != null){
                                    if(response.errorBody().string().contains(favouriteAddressesActivity.getResources()
                                            .getString(R.string.token_expired))){
                                        refreshToken();
                                    }
                                }
                                else{
                                    if(response.body().getContent().equals("Successfully delete favourite address")){
                                        favouriteAddressesActivity.finish();
                                        favouriteAddressesActivity.startActivity(favouriteAddressesActivity.getIntent());
                                        Toast.makeText(favouriteAddressesActivity,
                                                "Successfully delete favourite address",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(favouriteAddressesActivity,
                                                "Error delete favourite address",
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
                        favouriteAddressesActivity.runOnUiThread(() ->
                                Toast.makeText(favouriteAddressesActivity, "Failed to delete favourite addresses info",
                                Toast.LENGTH_SHORT).show());
                        Log.d("get favourite address log", "Failed to delete favourite addresses info");
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
                            if(registerResponse.getAccessToken().equals(favouriteAddressesActivity.getResources()
                                    .getString(R.string.token_expired))){
                                goLogin();
                            }
                            else if(registerResponse.getAccessToken().equals(favouriteAddressesActivity.getResources()
                                    .getString(R.string.username_not_found))){
                                goLogin();
                            }
                            else {
                                UserInfoService.addProperty("access_token", registerResponse.getAccessToken());
                                UserInfoService.addProperty("refresh_token", registerResponse.getRefreshToken());
                                favouriteAddressesActivity.finish();
                                favouriteAddressesActivity.startActivity(favouriteAddressesActivity.getIntent());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        favouriteAddressesActivity.runOnUiThread(() ->
                                Toast.makeText(favouriteAddressesActivity, "Failed to check user authentication",
                                Toast.LENGTH_SHORT).show());

                    }
                });
    }
    private void goLogin() {
        Intent intent = new Intent(favouriteAddressesActivity, UserLoginActivity.class);
        favouriteAddressesActivity.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return favouriteAddressesUserInfoList.size();
    }
}
