package com.example.minitaxiandroid.adapter;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.minitaxiandroid.R;

public class UserOrderHistoryHolder extends  RecyclerView.ViewHolder {

    public TextView driverName, carName, addressCustomer, addressDelivery, price, date;
    public RatingBar ratingOrderBar;
    public ConstraintLayout constraintLayout;

    public UserOrderHistoryHolder(@NonNull View itemView) {
        super(itemView);
        driverName = itemView.findViewById(R.id.userOrderHistoryDriverName);
        carName = itemView.findViewById(R.id.userOrderHistoryCarName);
        addressCustomer = itemView.findViewById(R.id.userOrderHistoryOrderAddress);
        addressDelivery = itemView.findViewById(R.id.userOrderHistoryDeliveryAddress);
        price = itemView.findViewById(R.id.userOrderHistoryPrice);
        date = itemView.findViewById(R.id.userOrderHistoryDate);
        ratingOrderBar = itemView.findViewById(R.id.userOrderHistoryRating);
        constraintLayout = itemView.findViewById(R.id.userOrderHistoryRecycleView);
    }
}
