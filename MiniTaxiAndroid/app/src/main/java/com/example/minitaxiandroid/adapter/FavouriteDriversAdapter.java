package com.example.minitaxiandroid.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.minitaxiandroid.R;
import com.example.minitaxiandroid.entities.userinfo.FavouriteDriverUserInfo;
import com.example.minitaxiandroid.retrofit.SelectListener;

import java.text.DecimalFormat;
import java.util.List;

public class FavouriteDriversAdapter extends RecyclerView.Adapter<FavouriteDriversHolder>{

    private List<FavouriteDriverUserInfo> favouriteDriverUserInfoList;
    private SelectListener selectListener;

    public FavouriteDriversAdapter(List<FavouriteDriverUserInfo> favouriteDriverUserInfoList, SelectListener selectListener) {
        this.favouriteDriverUserInfoList = favouriteDriverUserInfoList;
        this.selectListener = selectListener;
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
        favouriteDriversHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectListener.onItemClicked(favouriteDriverUserInfoList.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
       return favouriteDriverUserInfoList.size();
    }
}