package com.example.minitaxiandroid.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.minitaxiandroid.R;
import com.example.minitaxiandroid.entities.messages.OrderInfo;
import com.example.minitaxiandroid.retrofit.SelectListener;

import java.util.List;

public class OrderInfoProfileAdapter extends RecyclerView.Adapter<OrderInfoProfileHolder>{

    private List<OrderInfo> orderInfoList;
    private SelectListener selectListener;

    public OrderInfoProfileAdapter(List<OrderInfo> orderInfoList, SelectListener selectListener) {
        this.orderInfoList = orderInfoList;
        this.selectListener = selectListener;
    }

    @NonNull
    @Override
    public OrderInfoProfileHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_orderinfo_profile_item, viewGroup, false);
        return new OrderInfoProfileHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderInfoProfileHolder orderInfoProfileHolder, @SuppressLint("RecyclerView") int i) {
        OrderInfo orderInfo = orderInfoList.get(i);
        String date = String.valueOf(orderInfo.getDate());
        orderInfoProfileHolder.orderDate.setText(date);
        String rating = String.valueOf(orderInfo.getRating());
        orderInfoProfileHolder.orderRating.setText(rating);
        String price = String.valueOf(orderInfo.getPrice());
        orderInfoProfileHolder.orderPrice.setText(price);
        String numberOfKilometers = String.valueOf(orderInfo.getNumberOfKilometers());
        orderInfoProfileHolder.orderNumberOfKilometers.setText(numberOfKilometers);
        orderInfoProfileHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectListener.onItemClicked(orderInfoList.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderInfoList.size();
    }
}
