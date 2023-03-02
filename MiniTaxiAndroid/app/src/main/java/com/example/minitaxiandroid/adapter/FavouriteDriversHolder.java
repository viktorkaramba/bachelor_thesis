package com.example.minitaxiandroid.adapter;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.minitaxiandroid.R;

public class FavouriteDriversHolder  extends  RecyclerView.ViewHolder{
    public TextView fullName, carName, countsOrder, averageRating;
    public ConstraintLayout constraintLayout;

    public FavouriteDriversHolder(@NonNull View itemView) {
        super(itemView);
        fullName = itemView.findViewById(R.id.favouriteDriversFullname);
        carName = itemView.findViewById(R.id.favouriteDriversCarName);
        countsOrder = itemView.findViewById(R.id.favouriteDriversCountsOrder);
        averageRating = itemView.findViewById(R.id.favouriteDriversAverageRating);
        constraintLayout = itemView.findViewById(R.id.favouriteDriversRecycleView);
    }
}
