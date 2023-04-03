package com.example.minitaxiandroid.retrofit;

import com.example.minitaxiandroid.entities.User;
import com.example.minitaxiandroid.entities.document.DriverLocation;
import com.example.minitaxiandroid.entities.document.DriverProfileInfo;
import com.example.minitaxiandroid.entities.document.DriverRecAnswer;
import com.example.minitaxiandroid.entities.document.DriverResume;
import com.example.minitaxiandroid.entities.messages.CarRecommendationInfo;
import com.example.minitaxiandroid.entities.messages.Message;
import com.example.minitaxiandroid.entities.messages.OrderInfo;
import com.example.minitaxiandroid.entities.messages.UserPickCar;
import com.example.minitaxiandroid.entities.ranks.EliteRankUserInfo;
import com.example.minitaxiandroid.entities.ranks.Rank;
import com.example.minitaxiandroid.entities.userinfo.*;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface MiniTaxiApi {
    @GET("/userpickcar")
    Call<List<UserPickCar>> getUserPickCar();

    @POST("/driverresume")
    Call<DriverResume> saveDriverResume(@Body DriverResume driverResume);

    @POST("/users")
    Call<User> saveUser(@Body User user);

    @GET("/driver-profile/{id}")
    Call<DriverProfileInfo> getDriverInfo(@Path(value ="id", encoded = true) String id);

    @GET("/order-info-profile/{id}")
    Call<List<OrderInfo>> getOderInfoDriver(@Path(value ="id", encoded = true) String id);

    @GET("/drivers-cars-recommendations/{id}")
    Call<CarRecommendationInfo> getDriverCarRecommendations(@Path(value ="id", encoded = true) String id);

    @POST("/drivers-cars-recommendations-driver-answer")
    Call<Message> getAnswer(@Body DriverRecAnswer driverRecAnswer);

    @GET("/drivers-locations")
    Call<List<DriverLocation>> getDriverLocation();

    @GET("/user-order-history/{id}")
    Call<List<UserOrderInfo>> getUserOrderHistory(@Path(value ="id", encoded = true) String id);

    @GET("/favourite_drivers/{id}")
    Call<List<FavouriteDriverUserInfo>> getFavouriteDriverUserInfo(@Path(value ="id", encoded = true) String id);

    @GET("/favourite_drivers")
    Call<Message> getFavouriteDriverUserInfo(@Body FavouriteDriver favouriteDriver);

    @DELETE("/favourite_drivers/{id}")
    Call<Message> deleteFavouriteDriverUserInfo(@Path(value ="id", encoded = true) String id);

    @GET("/favourite_addresses/{id}")
    Call<List<FavouriteAddressesUserInfo>> getFavouriteAddressesUserInfo(@Path(value ="id", encoded = true) String id);

    @POST("/favourite_addresses")
    Call<Message> addFavouriteAddressesUserInfo(@Body FavouriteAddress favouriteAddress);

    @DELETE("/favourite_addresses/{id}")
    Call<Message> deleteFavouriteAddressesUserInfo(@Path(value ="id", encoded = true) String id);

    @POST("/drivers-locations")
    Call<DriverLocation> saveDriverLocation(@Body DriverLocation driverLocation);

    @PUT("/drivers-locations")
    Call<DriverLocation> updateDriverLocation(@Body DriverLocation driverLocation);

    @GET("/ranks")
    Call<List<Rank>> getRankInfo();

    @GET("/elite-ranks-user-info")
    Call<List<EliteRankUserInfo>> getEliteRanksInfo();

    @GET("/api/0.6/map")
    Call<ResponseBody> getMapData(@Query("bbox") String bbox, @Query("format") String format);

}
