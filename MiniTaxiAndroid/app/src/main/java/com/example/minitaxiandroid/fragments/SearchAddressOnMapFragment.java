package com.example.minitaxiandroid.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.minitaxiandroid.R;
import com.example.minitaxiandroid.activities.MainActivity;
import com.example.minitaxiandroid.activities.UserLoginActivity;
import com.example.minitaxiandroid.api.MiniTaxiApi;
import com.example.minitaxiandroid.api.RetrofitService;
import com.example.minitaxiandroid.entities.auth.RegisterResponse;
import com.example.minitaxiandroid.entities.messages.MyMessage;
import com.example.minitaxiandroid.entities.userinfo.FavouriteAddress;
import com.example.minitaxiandroid.services.UserInfoService;
import com.like.LikeButton;
import com.like.OnLikeListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;


public class SearchAddressOnMapFragment extends Fragment {

    private Button applyAddressOnMapButton;
    private TextView selectedAddressOnMapText;
    private Button backAddressOnMapButton;
    private LikeButton addressMapLikeButton;
    private MainActivity activity;
    private String side, fromAddress, toAddress, favouriteDriver = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_address_on_map,
                container, false);
        activity = (MainActivity) getActivity();
        activity.setMapMarkerVisible(true);
        UserInfoService.init(SearchAddressOnMapFragment.this.getContext());
        if (getArguments()!=null) {
            side = getArguments().getString("side");
            fromAddress = getArguments().getString("fromAddress");
            toAddress = getArguments().getString("toAddress");
            favouriteDriver = getArguments().getString("favouriteDriver");
        }
        selectedAddressOnMapText = view.findViewById(R.id.selectedAddressOnMapText);
        applyAddressOnMapButton = view.findViewById(R.id.applyAddressOnMapButton);
        backAddressOnMapButton = view.findViewById(R.id.backAddressOnMapButton);
        addressMapLikeButton = view.findViewById(R.id.addressMapLikeButton);
        backAddressOnMapButton.setOnClickListener(view12 -> back());
        addressMapLikeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                likeAddress();
            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });
        applyAddressOnMapButton.setOnClickListener(view1 -> {
            if(selectedAddressOnMapText.getText().equals(getResources().
                    getString(R.string.click_tos_select_address_map))){
                Toast.makeText(activity.getApplicationContext(), "You dont select a address", Toast.LENGTH_LONG).show();
            }
            else {
                applyAddress();
            }
        });
        return view;
    }

    private void likeAddress() {
        if(selectedAddressOnMapText.getText().toString().equals(getResources().getString(R.string.click_tos_select_address_map))){
            Toast.makeText(activity.getApplicationContext(), "You dont select a address", Toast.LENGTH_LONG).show();
            addressMapLikeButton.setLiked(false);
        }
        else {
            addressMapLikeButton.setEnabled(false);
            addressMapLikeButton.setLiked(true);
            addAddress();
        }
    }

    public void setLike(){
        addressMapLikeButton.setLiked(true);
        addressMapLikeButton.setEnabled(false);
    }

    private void addAddress() {
        FavouriteAddress favouriteAddress = new FavouriteAddress();
        favouriteAddress.setUserId(Integer.parseInt(UserInfoService.getProperty("userId")));
        favouriteAddress.setAddress(selectedAddressOnMapText.getText().toString());
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi favouriteAddressApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        favouriteAddressApi.addFavouriteAddressesUserInfo("Bearer " + UserInfoService.getProperty("access_token"),
                        favouriteAddress)
                .enqueue(new Callback<MyMessage>() {
                    @Override
                    public void onResponse(Call<MyMessage> call, Response<MyMessage> response) {
                        if(response.body() != null) {
                            try {
                                if (response.errorBody() != null) {
                                    if (response.errorBody().string().contains(getResources()
                                            .getString(R.string.token_expired))) {
                                        refreshToken();
                                    }
                                } else {
                                    if (response.body().getContent().equals("Successfully added favourite address")) {
                                        activity.addNewAddressToList(selectedAddressOnMapText.getText().toString());
                                        Toast.makeText(activity,
                                                "Successfully delete favourite address",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity,
                                                "Error added favourite address",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MyMessage> call, Throwable t) {
                        addressMapLikeButton.setLiked(false);
                        activity.runOnUiThread(() ->
                                Toast.makeText(SearchAddressOnMapFragment.this.getContext(),
                                        "Failed to add favourite address",
                                Toast.LENGTH_SHORT).show());
                    }
                });
    }

    public void setAddressText(String addressText){
        selectedAddressOnMapText.setText(addressText);
    }

    public void setClickableLikeButton(){
        addressMapLikeButton.setEnabled(true);
        addressMapLikeButton.setLiked(false);
    }

    private void applyAddress(){
        activity.setMapMarkerVisible(false);
        activity.removeMarker();
        Bundle bundle = new Bundle();
        if(side.equals("From")){
            fromAddress = selectedAddressOnMapText.getText().toString();
        }
        else {
            toAddress = selectedAddressOnMapText.getText().toString();
        }
        System.out.println(toAddress);
        bundle.putString("fromAddress", fromAddress);
        bundle.putString("toAddress", toAddress);
        bundle.putString("favouriteDriver", favouriteDriver);
        SearchAddressesFragment searchAddressesFragment = new SearchAddressesFragment();
        searchAddressesFragment.setArguments(bundle);
        replaceFragment(searchAddressesFragment);
    }

    private void back(){
        activity.setMapMarkerVisible(false);
        activity.removeMarker();
        Bundle bundle = new Bundle();
        bundle.putString("fromAddress", fromAddress);
        bundle.putString("toAddress", toAddress);
        bundle.putString("favouriteDriver", favouriteDriver);
        SearchAddressesFragment searchAddressesFragment = new SearchAddressesFragment();
        searchAddressesFragment.setArguments(bundle);
        replaceFragment(searchAddressesFragment);
    }
    private void replaceFragment(SearchAddressesFragment searchAddressesFragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, searchAddressesFragment);
        fragmentTransaction.commit();
    }

    private void goLogin() {
        Intent intent = new Intent(this.getContext(), UserLoginActivity.class);
        startActivity(intent);
    }
    private void refreshToken() {
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi api = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        api.refreshToken("Bearer " + UserInfoService.getProperty("refresh_token"))
                .enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                        if(response.body() != null){
                            RegisterResponse registerResponse = response.body();
                            if(registerResponse.getAccessToken().equals(getResources()
                                    .getString(R.string.token_expired))){
                                goLogin();
                            }
                            else if(registerResponse.getAccessToken().equals(getResources()
                                    .getString(R.string.username_not_found))){
                                goLogin();
                            }
                            else {
                                UserInfoService.addProperty("access_token", registerResponse.getAccessToken());
                                UserInfoService.addProperty("refresh_token", registerResponse.getRefreshToken());
                                back();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        SearchAddressOnMapFragment.this.getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(SearchAddressOnMapFragment.this.getContext(),
                                        "Failed to check user authentication",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }
}