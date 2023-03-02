package com.example.minitaxiandroid.adapter;


import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.minitaxiandroid.R;

public class FavouriteAddressesHolder extends  RecyclerView.ViewHolder{

    public TextView addressName, countsOrders;
    public ConstraintLayout constraintLayout;

    public FavouriteAddressesHolder(@NonNull View itemView) {
        super(itemView);
        addressName = itemView.findViewById(R.id.favouriteAddressesName);
        countsOrders = itemView.findViewById(R.id.favouriteAddressesCountOrders);
        constraintLayout = itemView.findViewById(R.id.favouriteAddressRecycleView);
    }
}
