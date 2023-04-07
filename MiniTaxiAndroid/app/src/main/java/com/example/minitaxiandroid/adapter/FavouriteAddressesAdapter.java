package com.example.minitaxiandroid.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.minitaxiandroid.R;
import com.example.minitaxiandroid.entities.messages.Message;
import com.example.minitaxiandroid.entities.userinfo.FavouriteAddressesUserInfo;
import com.example.minitaxiandroid.retrofit.MiniTaxiApi;
import com.example.minitaxiandroid.retrofit.RetrofitService;
import com.example.minitaxiandroid.retrofit.SelectListener;
import com.example.minitaxiandroid.services.UserLoginInfoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class FavouriteAddressesAdapter extends RecyclerView.Adapter<FavouriteAddressesHolder>{

    private List<FavouriteAddressesUserInfo> favouriteAddressesUserInfoList;
    private SelectListener selectListener;

    public FavouriteAddressesAdapter(List<FavouriteAddressesUserInfo> favouriteAddressesUserInfoList, SelectListener selectListener) {
        this.favouriteAddressesUserInfoList = favouriteAddressesUserInfoList;
        this.selectListener = selectListener;
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
        favouriteAddressesHolder.deleteFavouriteAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAddress(favouriteAddressesUserInfo.getAddress());
            }
        });
        favouriteAddressesHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectListener.onItemClicked(favouriteAddressesUserInfoList.get(i));
            }
        });
    }

    private void deleteAddress(String address) {
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi favouriteAddressApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        String userId = UserLoginInfoService.getProperty("userId");
        favouriteAddressApi.deleteFavouriteAddressesUserInfo(Integer.valueOf(userId), address)
                .enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        String message = response.toString();
                        Log.d("get favourite address log", message);
                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        Log.d("get favourite address log", "Failed to delete favourite addresses info");
                    }
                });
    }

    @Override
    public int getItemCount() {
        return favouriteAddressesUserInfoList.size();
    }
}
