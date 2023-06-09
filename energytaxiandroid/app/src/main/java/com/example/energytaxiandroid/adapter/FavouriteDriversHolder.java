package com.example.energytaxiandroid.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.energytaxiandroid.R;

public class FavouriteDriversHolder  extends  RecyclerView.ViewHolder{
    public TextView fullName, carName, countsOrder, averageRating;
    public Button deleteFavouriteDriverButton;
    public ConstraintLayout constraintLayout;

    public FavouriteDriversHolder(@NonNull View itemView) {
        super(itemView);
        fullName = itemView.findViewById(R.id.favouriteDriversFullname);
        carName = itemView.findViewById(R.id.favouriteDriversCarName);
        countsOrder = itemView.findViewById(R.id.favouriteDriversCountsOrder);
        averageRating = itemView.findViewById(R.id.favouriteDriversAverageRating);
        deleteFavouriteDriverButton = itemView.findViewById(R.id.deleteFavouriteDriverButton);
        constraintLayout = itemView.findViewById(R.id.favouriteDriversRecycleView);
    }
}
