package com.example.minitaxiandroid.retrofit;

import com.example.minitaxiandroid.entities.messages.OrderInfo;
import com.example.minitaxiandroid.entities.messages.UserPickCar;
import com.example.minitaxiandroid.entities.userinfo.FavouriteAddressesUserInfo;
import com.example.minitaxiandroid.entities.userinfo.FavouriteDriverUserInfo;
import com.example.minitaxiandroid.entities.userinfo.UserOrderInfo;

public interface SelectListener {
    void onItemClicked(UserPickCar userPickCar);
    void onItemClicked(OrderInfo orderInfo);
    void onItemClicked(FavouriteDriverUserInfo favouriteDriverUserInfo);
    void onItemClicked(FavouriteAddressesUserInfo favouriteAddressesUserInfo);
    void onItemClicked(UserOrderInfo userOrderInfo);
}
