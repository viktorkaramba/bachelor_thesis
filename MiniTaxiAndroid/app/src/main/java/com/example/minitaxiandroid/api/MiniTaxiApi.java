package com.example.minitaxiandroid.api;

import com.example.minitaxiandroid.entities.auth.ChangeUserDataRequest;
import com.example.minitaxiandroid.entities.auth.LoginResponse;
import com.example.minitaxiandroid.entities.auth.RegisterResponse;
import com.example.minitaxiandroid.entities.auth.UserRequest;
import com.example.minitaxiandroid.entities.bonuses.MilitaryBonuses;
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

    @POST("/api/v1/auth/check-token")
    Call<ResponseMessage> checkUserToken(@Header("Authorization") String header);

    @POST("/api/v1/auth/refresh-token")
    Call<RegisterResponse> refreshToken(@Header("Authorization") String header);

    @POST("/api/v1/auth/user-register")
    Call<RegisterResponse> userRegister(@Body UserRequest request);

    @POST("/api/v1/auth/driver-register")
    Call<MyMessage> driverRegister(@Body DriverResume driverResume);

    @POST("/api/v1/auth/user-authenticate")
    Call<LoginResponse> authenticateUser(@Body UserRequest request);

    @POST("/api/v1/auth/driver-authenticate")
    Call<LoginResponse> authenticateDriver(@Body UserRequest request);

    @POST("/api/v1/auth/change-password")
    Call<MyMessage> changePassword(@Header("Authorization") String header, @Body ChangeUserDataRequest changeUserDataRequest);

    @POST("/api/v1/auth/change-username")
    Call<LoginResponse> changeUserName(@Header("Authorization") String header, @Body MyMessage message);

    @POST("/api/v1/auth/reset-password")
    Call<MyMessage> resetPassword(@Body ChangeUserDataRequest changeUserDataRequest);

    @POST("/api/v1/auth/reset-password-opt")
    Call<LoginResponse> resetPasswordOPT(@Body ChangeUserDataRequest changeUserDataRequest);

    @GET("/api/v1/auth/logout")
    Call<RegisterResponse> logout(@Header("Authorization") String header);

    @GET("/api/v1/driver-app/driver-profile/{id}")
    Call<DriverProfileInfo> getDriverInfo(@Header("Authorization") String header,
                                          @Path(value ="id", encoded = true) String id);

    @GET("/api/v1/user-app/user-profile-info/{id}")
    Call<UserProfileInfo> getUserProfileInfo(@Header("Authorization") String header, @Path(value ="id", encoded = true) String id);

    @GET("/api/v1/driver-app/order-info-profile/{id}")
    Call<List<OrderInfo>> getOderInfoDriver(@Header("Authorization") String header,
                                            @Path(value ="id", encoded = true) String id);

    @GET("/api/v1/driver-app/drivers-cars-recommendations/{id}")
    Call<CarRecommendationInfo> getDriverCarRecommendations(@Header("Authorization") String header,
                                                            @Path(value ="id", encoded = true) String id);

    @POST("/api/v1/driver-app/drivers-cars-recommendations-driver-answer")
    Call<MyMessage> getAnswer(@Header("Authorization") String header,
                              @Body DriverRecAnswer driverRecAnswer);


    @GET("/api/v1/user-app/user-order-history/{id}")
    Call<List<UserOrderInfo>> getUserOrderHistory(@Header("Authorization") String header,
                                                  @Path(value ="id", encoded = true) String id);

    @GET("/api/v1/user-app/favourite-drivers/{id}")
    Call<List<FavouriteDriverUserInfo>> getFavouriteDriverUserInfo(@Header("Authorization") String header,
                                                                   @Path(value ="id", encoded = true) Integer id);

    @POST("/api/v1/user-app/favourite-drivers")
    Call<MyMessage> addFavouriteDriver(@Header("Authorization") String header,
                                                  @Body FavouriteDriver favouriteDriver);

    @DELETE("/api/v1/user-app/favourite-drivers-by-driverId-userId/{driverId}/{userId}")
    Call<MyMessage> deleteFavouriteDriverUserInfo(@Header("Authorization") String header,
                                                  @Path(value ="driverId", encoded = true) Integer driverId,
                                                  @Path(value ="userId", encoded = true) Integer userId);
    @GET("/api/v1/user-app/favourite-addresses/{id}")
    Call<List<FavouriteAddressesUserInfo>> getFavouriteAddressesUserInfo(@Header("Authorization") String header,
                                                                         @Path(value ="id", encoded = true) Integer id);

    @POST("/api/v1/user-app/favourite-addresses")
    Call<MyMessage> addFavouriteAddressesUserInfo(@Header("Authorization") String header,
                                                  @Body FavouriteAddress favouriteAddress);

    @DELETE("/api/v1/user-app/favourite-addresses-by-userId-address/{userId}/{address}")
    Call<MyMessage> deleteFavouriteAddressesUserInfo(@Header("Authorization") String header,
                                                     @Path(value ="userId", encoded = true) Integer userId,
                                                     @Path(value ="address", encoded = true) String address);

    @POST("/api/v1/user-app/user-order-price-by-class")
    Call<PriceByClassResponse> getUserOrderPriceByClass(@Header("Authorization") String header,
                                                        @Body PriceByClassRequest priceByClassRequest);

    @GET("/api/v1/bonuses/ranks")
    Call<List<Rank>> getRankInfo(@Header("Authorization") String header);

    @GET("/api/v1/bonuses/elite-ranks-user-info")
    Call<List<EliteRankUserInfo>> getEliteRanksInfo(@Header("Authorization") String header);

    @GET("/api/v1/user-app/user-stats/{userId}")
    Call<UserStats> getUserStats(@Header("Authorization") String header,
                                 @Path(value ="userId", encoded = true) Integer userId);

    @GET("/api/v1/bonuses/user-rank-achievements-info/{userId}/{rankId}")
    Call<UserRankAchievementInfo> getUserRankAchievementsInfo(@Header("Authorization") String header,
                                                              @Path(value ="userId", encoded = true) Integer userId,
                                                              @Path(value ="rankId", encoded = true) Integer rankId);

    @GET("/api/v1/bonuses/user-elite-rank-achievements-info/{userId}/{rankId}")
    Call<List<UserEliteRankAchievementInfo>> getUserEliteRankAchievementsInfo(@Header("Authorization") String header,
                                                                              @Path(value ="userId", encoded = true) Integer userId,
                                                                              @Path(value ="rankId", encoded = true) Integer rankId);

    @GET("/api/v1/bonuses/user-elite-rank-achievements-info-by-driver/{userId}/{rankId}/{driverId}")
    Call<List<UserEliteRankAchievementInfo>> getUserEliteRankAchievementsInfoByDriver(@Header("Authorization") String header,
                                                                                      @Path(value ="userId", encoded = true) Integer userId,
                                                                                      @Path(value ="rankId", encoded = true) Integer rankId,
                                                                                      @Path(value ="driverId", encoded = true) Integer driverId);

    @GET("/api/v1/bonuses/military-bonuses/{id}")
    Call<MilitaryBonuses> getMilitaryBonuses(@Header("Authorization") String header,
                                             @Path(value ="id", encoded = true) Integer id);

    @POST("/api/v1/bonuses/military-bonuses")
    Call<MyMessage> addMilitaryBonuses(@Header("Authorization") String header,
                                       @Body MilitaryBonuses militaryBonuses);

    @DELETE("/api/v1/bonuses/military-bonuses/{userId}")
    Call<MyMessage> deleteMilitaryBonuses(@Header("Authorization") String header,
                                          @Path(value ="userId", encoded = true) Integer userId);

    @POST("/api/v1/user-app/orders")
    Call<String> completeOrder(@Header("Authorization") String header,
                               @Body SendOrder sendOrder);
}
