package com.example.energytaxiandroid.api;

import com.example.energytaxiandroid.constants.Links;
import com.google.gson.Gson;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private Retrofit retrofit;

    public RetrofitService(){
        initializeRetrofit();
    }

    public void initializeRetrofit(){
        retrofit = new Retrofit.Builder()
                .baseUrl(Links.HTTP_HOST)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
