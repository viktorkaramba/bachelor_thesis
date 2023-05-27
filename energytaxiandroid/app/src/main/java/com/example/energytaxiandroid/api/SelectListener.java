package com.example.energytaxiandroid.api;

import com.example.energytaxiandroid.entities.messages.OrderInfo;
import com.example.energytaxiandroid.entities.userinfo.FavouriteAddressesUserInfo;
import com.example.energytaxiandroid.entities.userinfo.FavouriteDriverUserInfo;
import com.example.energytaxiandroid.entities.userinfo.UserOrderInfo;

public interface SelectListener {
    void onItemClicked(OrderInfo orderInfo);
    void onItemClicked(FavouriteDriverUserInfo favouriteDriverUserInfo);
    void onItemClicked(FavouriteAddressesUserInfo favouriteAddressesUserInfo);
    void onItemClicked(UserOrderInfo userOrderInfo);
}
