package com.example.minitaxiandroid.fragments;

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
import com.example.minitaxiandroid.entities.messages.Message;
import com.example.minitaxiandroid.entities.userinfo.FavouriteAddress;
import com.example.minitaxiandroid.retrofit.MiniTaxiApi;
import com.example.minitaxiandroid.retrofit.RetrofitService;
import com.example.minitaxiandroid.services.UserLoginInfoService;
import com.like.LikeButton;
import com.like.OnLikeListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchAddressOnMapFragment extends Fragment {

    private Button applyAddressOnMapButton;
    private TextView selectedAddressOnMapText;
    private Button backAddressOnMapButton;
    private LikeButton addressMapLikeButton;
    private MainActivity activity;
    private String message;
    private String otherAddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_address_on_map,
                container, false);
        activity = (MainActivity) getActivity();
        activity.setMapMarkerVisible(true);
        if (getArguments()!=null) {
            message = getArguments().getString("messageFrom");
            otherAddress = getArguments().getString("otherAddress");
        }
        selectedAddressOnMapText = view.findViewById(R.id.selectedAddressOnMapText);
        applyAddressOnMapButton = view.findViewById(R.id.applyAddressOnMapButton);
        backAddressOnMapButton = view.findViewById(R.id.backAddressOnMapButton);
        addressMapLikeButton = view.findViewById(R.id.addressMapLikeButton);
        backAddressOnMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        addressMapLikeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                likeAddress();
            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });
        applyAddressOnMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            addAddress();
        }
    }

    public void setLike(){
        addressMapLikeButton.setLiked(true);
        addressMapLikeButton.setEnabled(false);
    }

    private void addAddress() {
        FavouriteAddress favouriteAddress = new FavouriteAddress();
        favouriteAddress.setUserId(Integer.parseInt(UserLoginInfoService.getProperty("userId")));
        favouriteAddress.setAddress(selectedAddressOnMapText.getText().toString());
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi favouriteAddressApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        favouriteAddressApi.addFavouriteAddressesUserInfo(favouriteAddress)
                .enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        Toast.makeText(SearchAddressOnMapFragment.this.getContext(), "Added new favourite driver",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        addressMapLikeButton.setLiked(false);
                        Toast.makeText(SearchAddressOnMapFragment.this.getContext(), "Failed to add favourite drivers info",
                                Toast.LENGTH_SHORT).show();
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
        bundle.putString("messageTo", message);
        bundle.putString("otherAddress", otherAddress);
        bundle.putString("address", selectedAddressOnMapText.getText().toString());
        SearchAddressesFragment searchAddressesFragment = new SearchAddressesFragment();
        searchAddressesFragment.setArguments(bundle);
        replaceFragment(searchAddressesFragment);
    }

    private void back(){
        activity.setMapMarkerVisible(false);
        activity.removeMarker();
        Bundle bundle = new Bundle();
        bundle.putString("messageTo", message);
        bundle.putString("otherAddress", otherAddress);
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
}