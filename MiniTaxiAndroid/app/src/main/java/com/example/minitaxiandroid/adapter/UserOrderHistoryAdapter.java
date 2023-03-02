package com.example.minitaxiandroid.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.minitaxiandroid.R;
import com.example.minitaxiandroid.entities.userinfo.UserOrderInfo;
import com.example.minitaxiandroid.retrofit.SelectListener;

import java.util.List;

public class UserOrderHistoryAdapter extends RecyclerView.Adapter<UserOrderHistoryHolder> {

    private List<UserOrderInfo> userOrderInfoList;
    private SelectListener selectListener;

    public UserOrderHistoryAdapter(List<UserOrderInfo> userOrderInfoList, SelectListener selectListener){
        this.userOrderInfoList = userOrderInfoList;
        this.selectListener = selectListener;
    }

    @NonNull
    @Override
    public UserOrderHistoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_order_history_item, viewGroup, false);
        return new UserOrderHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserOrderHistoryHolder userOrderHistoryHolder,
                                 @SuppressLint("RecyclerView") int i) {
        UserOrderInfo userOrderInfo = userOrderInfoList.get(i);
        userOrderHistoryHolder.driverName.setText(userOrderInfo.getDriverName());
        userOrderHistoryHolder.carName.setText(userOrderInfo.getCarName());
        userOrderHistoryHolder.addressCustomer.setText(userOrderInfo.getAddressCustomer());
        userOrderHistoryHolder.addressDelivery.setText(userOrderInfo.getAddressDelivery());
        userOrderHistoryHolder.ratingOrderBar.setRating(userOrderInfo.getRating());
        userOrderHistoryHolder.price.setText(String.valueOf(userOrderInfo.getPrice()));
        userOrderHistoryHolder.date.setText(String.valueOf(userOrderInfo.getDate()));
        userOrderHistoryHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectListener.onItemClicked(userOrderInfoList.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return userOrderInfoList.size();
    }
}
