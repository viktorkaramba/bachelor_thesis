package com.example.energytaxiandroid.adapter;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.energytaxiandroid.R;

public class OrderInfoProfileHolder extends  RecyclerView.ViewHolder{
    public TextView orderDate, orderRating, orderPrice, orderNumberOfKilometers;
    public ConstraintLayout constraintLayout;
    public OrderInfoProfileHolder(@NonNull View itemView) {
        super(itemView);
        orderDate = itemView.findViewById(R.id.orderInfoProfileDateText);
        orderRating = itemView.findViewById(R.id.orderInfoProfileRatingText);
        orderPrice = itemView.findViewById(R.id.orderInfoProfilePriceText);
        orderNumberOfKilometers = itemView.findViewById(R.id.orderInfoProfileNOfKText);
        constraintLayout = itemView.findViewById(R.id.orderInfoProfileRecycleView);
    }
}
