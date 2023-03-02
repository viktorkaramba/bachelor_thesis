package com.example.minitaxiandroid.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.minitaxiandroid.R;
import com.example.minitaxiandroid.entities.userinfo.FavouriteAddressesUserInfo;
import com.example.minitaxiandroid.retrofit.SelectListener;

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
        favouriteAddressesHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectListener.onItemClicked(favouriteAddressesUserInfoList.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return favouriteAddressesUserInfoList.size();
    }
}
