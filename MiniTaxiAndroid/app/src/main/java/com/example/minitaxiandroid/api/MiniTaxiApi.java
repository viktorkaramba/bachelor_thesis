package com.example.minitaxiandroid.api;

import com.example.minitaxiandroid.entities.User;
import com.example.minitaxiandroid.entities.bonuses.MilitaryBonuses;
import com.example.minitaxiandroid.entities.document.DriverLocation;
import com.example.minitaxiandroid.entities.document.DriverProfileInfo;
import com.example.minitaxiandroid.entities.document.DriverRecAnswer;
import com.example.minitaxiandroid.entities.document.DriverResume;
import com.example.minitaxiandroid.entities.messages.*;
import com.example.minitaxiandroid.entities.ranks.EliteRankUserInfo;
import com.example.minitaxiandroid.entities.ranks.Rank;
import com.example.minitaxiandroid.entities.ranks.UserEliteRankAchievementInfo;
import com.example.minitaxiandroid.entities.ranks.UserRankAchievementInfo;
import com.example.minitaxiandroid.entities.userinfo.*;
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

    @GET("/favourite-drivers/{id}")
    Call<List<FavouriteDriverUserInfo>> getFavouriteDriverUserInfo(@Path(value ="id", encoded = true) Integer id);

    @GET("/favourite-drivers")
    Call<Message> getFavouriteDriverUserInfo(@Body FavouriteDriver favouriteDriver);

    @DELETE("/favourite-drivers-by-driverId-userId/{driverId}/{userId}")
    Call<Message> deleteFavouriteDriverUserInfo(@Path(value ="driverId", encoded = true) Integer driverId,
                                                @Path(value ="userId", encoded = true) Integer userId);

    @GET("/favourite-addresses/{id}")
    Call<List<FavouriteAddressesUserInfo>> getFavouriteAddressesUserInfo(@Path(value ="id", encoded = true) Integer id);

    @POST("/favourite-addresses")
    Call<Message> addFavouriteAddressesUserInfo(@Body FavouriteAddress favouriteAddress);

    @DELETE("/favourite-addresses-by-userId-address/{userId}/{address}")
    Call<Message> deleteFavouriteAddressesUserInfo(@Path(value ="userId", encoded = true) Integer userId,
                                                   @Path(value ="address", encoded = true) String address);

    @POST("/drivers-locations")
    Call<DriverLocation> saveDriverLocation(@Body DriverLocation driverLocation);

    @PUT("/drivers-locations")
    Call<DriverLocation> updateDriverLocation(@Body DriverLocation driverLocation);

    @GET("/user-order-price-by-class/{addressFrom}/{addressTo}")
    Call<PriceByClass> getUserOrderPriceByClass(@Path(value ="addressFrom", encoded = true) String addressFrom,
                                                @Path(value ="addressFrom", encoded = true) String addressTo);

    @GET("/ranks")
    Call<List<Rank>> getRankInfo();

    @GET("/elite-ranks-user-info")
    Call<List<EliteRankUserInfo>> getEliteRanksInfo();

    @GET("/user-stats/{userId}")
    Call<UserStats> getUserStats(@Path(value ="userId", encoded = true) Integer userId);

    @GET("/user-rank-achievements-info/{userId}/{rankId}")
    Call<UserRankAchievementInfo> getUserRankAchievementsInfo(@Path(value ="userId", encoded = true) Integer userId,
                                                              @Path(value ="rankId", encoded = true) Integer rankId);

    @GET("/user-elite-rank-achievements-info/{userId}/{rankId}")
    Call<List<UserEliteRankAchievementInfo>> getUserEliteRankAchievementsInfo(@Path(value ="userId", encoded = true) Integer userId,
                                                                              @Path(value ="rankId", encoded = true) Integer rankId);

    @GET("/user-elite-rank-achievements-info-by-driver/{userId}/{rankId}/{driverId}")
    Call<List<UserEliteRankAchievementInfo>> getUserEliteRankAchievementsInfoByDriver(@Path(value ="userId", encoded = true) Integer userId,
                                                                                      @Path(value ="rankId", encoded = true) Integer rankId,
                                                                                      @Path(value ="driverId", encoded = true) Integer driverId);

    @GET("/military-bonuses/{id}")
    Call<MilitaryBonuses> getMilitaryBonuses(@Path(value ="id", encoded = true) Integer id);

    @POST("/military-bonuses")
    Call<Message> addMilitaryBonuses(@Body MilitaryBonuses militaryBonuses);

    @DELETE("/military-bonuses/{userId}")
    Call<Message> deleteMilitaryBonuses(@Path(value ="userId", encoded = true) Integer userId);
}
