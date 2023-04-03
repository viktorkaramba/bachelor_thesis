package com.example.minitaxiandroid.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import com.example.minitaxiandroid.R;
import com.example.minitaxiandroid.entities.userinfo.DriverInfo;
import com.example.minitaxiandroid.entities.userinfo.FavouriteAddressesUserInfo;
import com.example.minitaxiandroid.fragments.SearchAddressOnMapFragment;
import com.example.minitaxiandroid.retrofit.MiniTaxiApi;
import com.example.minitaxiandroid.retrofit.RetrofitService;
import com.example.minitaxiandroid.services.DriverInfoService;
import com.example.minitaxiandroid.services.LocationService;
import com.example.minitaxiandroid.services.UserLoginInfoService;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.navigation.NavigationView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private FragmentContainerView fragmentContainerView;
//    private GoogleMap mMap;
    private Marker marker;
    private Fragment fragment;
//    private SupportMapFragment mapFragment;
    private LocationManager locationManager;
    private LocationService locationService;
    private boolean isMapFragment = false;
    private DriverInfoService driverInfoService;
    private ArrayList<String> favouriteAddressesList;

    public boolean isMapFragment() {
        return isMapFragment;
    }

    public void setMapFragment(boolean mapFragment) {
        isMapFragment = mapFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                PackageManager.PERMISSION_GRANTED);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_views);
        toolbar = findViewById(R.id.toolbar);
        fragmentContainerView = findViewById(R.id.fragmentContainerView);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle  = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        UserLoginInfoService.init(MainActivity.this);
        //TODO
        UserLoginInfoService.addProperty("isLogin",  "True");
        UserLoginInfoService.addProperty("userId", "1");
        UserLoginInfoService.addProperty("username", "viktor2002");
        UserLoginInfoService.addProperty("password", "driver");

        UserLoginInfoService.init(MainActivity.this);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView header = headerView.findViewById(R.id.headerProfileName);
        header.setText(UserLoginInfoService.getProperty("username"));
//        mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//        mapFragment.getView().setClickable(false);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationService = new LocationService(locationManager);
        driverInfoService = new DriverInfoService(this, locationService.getCurrentLocation());
        getFavouritesAddressesRequest();
    }

    public void showInfo(DriverInfo driverInfo){
        System.out.println("add marker2");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addMarker(new LatLng(Double.parseDouble(driverInfo.getLatitude()),
                        Double.parseDouble(driverInfo.getLongitude())));
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.nav_history:
                Intent user_order_history_intent = new Intent(MainActivity.this, UserOrderHistoryActivity.class);
                startActivity(user_order_history_intent);
                break;
            case R.id.nav_bonuses:
                Intent bonuses_intent = new Intent(MainActivity.this, RanksInfoActivity.class);
                startActivity(bonuses_intent);
                break;
            case R.id.nav_favourite_addresses:
                Intent fav_addresses_intent = new Intent(MainActivity.this, FavouriteAddressesActivity.class);
                startActivity(fav_addresses_intent);
                break;
            case R.id.nav_favourite_drivers:
                Intent fav_drivers_intent = new Intent(MainActivity.this, FavouriteDriversActivity.class);
                startActivity(fav_drivers_intent);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

//    @SuppressLint("MissingPermission")
//    @Override
//    public void onMapReady(@NonNull GoogleMap googleMap) {
////        mMap = googleMap;
////        LatLng newLatLng = new LatLng(locationService.getCurrentLocation().latitude,
////                locationService.getCurrentLocation().longitude);
////        mMap.moveCamera(CameraUpdateFactory.newLatLng(newLatLng));
////        mMap.setMyLocationEnabled(true);
////        mMap.getUiSettings().setMyLocationButtonEnabled(true);
////        mMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
////            @Override
////            public void onMyLocationClick(@NonNull Location location) {
////
////            }
////        });
////        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
////            @Override
////            public boolean onMyLocationButtonClick() {
////
////                return false;
////            }
////        });
////        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
////            @Override
////            public void onMapClick(@NonNull LatLng latLng) {
////                if (mapFragment.getView().isClickable()){
////                    addMarker(latLng);
////                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
////                    setFragment();
////                    addText(getAddressName(latLng));
//    if(checkIsFavourite(getAddressName(latLng))){
//        setLike();
//    }
//    else{
//        setLikeVisible();
//    }
////
////                }
////            }
////        });
////        driverInfoService.getDriverInfo();
////        addDrivers();
//    }

    private boolean checkIsFavourite(String address){
        for(String favouriteAddress: favouriteAddressesList){
            if(favouriteAddress.equals(address)){
                return true;
            }
        }
        return false;
    }

    private void setLike() {
        if (fragment instanceof SearchAddressOnMapFragment){
            SearchAddressOnMapFragment searchAddressOnMapFragment = (SearchAddressOnMapFragment)fragment;
            searchAddressOnMapFragment.setLike();
        }
        else {
            Log.d("fragment not SearchAddressOnMapFragment",
                    "in setLike fragment not SearchAddressOnMapFragment");
        }
    }

    private void setLikeVisible() {
        if (fragment instanceof SearchAddressOnMapFragment){
            SearchAddressOnMapFragment searchAddressOnMapFragment = (SearchAddressOnMapFragment)fragment;
            searchAddressOnMapFragment.setClickableLikeButton();
        }
        else {
            Log.d("fragment not SearchAddressOnMapFragment",
                    "in setLikeVisible fragment not SearchAddressOnMapFragment");
        }
    }

    public void setFragment(){
        fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
    }

    private void addText(String addressName) {
        if (fragment instanceof SearchAddressOnMapFragment){
            SearchAddressOnMapFragment searchAddressOnMapFragment = (SearchAddressOnMapFragment)fragment;
            searchAddressOnMapFragment.setAddressText(addressName);
        }
        else {
            Log.d("fragment not SearchAddressOnMapFragment",
                    "in addText fragment not SearchAddressOnMapFragment");
        }
    }

    public String getAddressName(LatLng location){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        String address = null;
        try {
            addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    public void addMarker(LatLng latLng){
//        if(marker != null){
//            marker.remove();
//        }
//        marker = mMap.addMarker(new MarkerOptions()
//                .position(latLng)
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
//                .title("Marker"));
    }

    public void removeMarker(){
        if(marker!=null){
            marker.remove();
        }
    }

    public void setMapMarkerVisible(boolean value){
//        mapFragment.getView().setClickable(value);
    }

    public void getFavouritesAddressesRequest(){
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi favouriteAddressApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        String userId = "1";
        //TODO
        favouriteAddressApi.getFavouriteAddressesUserInfo(userId)
                .enqueue(new Callback<List<FavouriteAddressesUserInfo>>() {
                    @Override
                    public void onResponse(Call<List<FavouriteAddressesUserInfo>> call,
                                           Response<List<FavouriteAddressesUserInfo>> response) {
                        parseFavouriteAddressesList(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<FavouriteAddressesUserInfo>> call, Throwable t) {
                        Toast.makeText(MainActivity.this,
                                "Failed to load favourite drivers info",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public ArrayList<String> getFavouriteAddressesList() {
        return favouriteAddressesList;
    }

    private void parseFavouriteAddressesList(List<FavouriteAddressesUserInfo> body) {
        favouriteAddressesList = new ArrayList<>();
        for(FavouriteAddressesUserInfo info: body){
            favouriteAddressesList.add(info.getAddress());
        }
    }


//    private void addDrivers() {
//        Handler handler = new Handler(Looper.getMainLooper());
//        handler.postDelayed(new Runnable() {
//            public void run() {
////                markerName.remove();
//                LatLng newLatLng = new LatLng(locationService.getCurrentLocation().latitude,
//                        locationService.getCurrentLocation().longitude);
//                marker = mMap.addMarker(new MarkerOptions()
//                        .position(newLatLng)
//                        .title("Marker"));
//                System.out.println("Move");
//                handler.postDelayed(this, 2000);
//            }
//        }, 0);
//    }

}
