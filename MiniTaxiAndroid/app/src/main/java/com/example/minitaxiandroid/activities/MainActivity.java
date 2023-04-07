package com.example.minitaxiandroid.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import com.example.minitaxiandroid.R;
import com.example.minitaxiandroid.entities.document.DRIVER_STATUS;
import com.example.minitaxiandroid.entities.userinfo.DriverInfo;
import com.example.minitaxiandroid.entities.userinfo.FavouriteAddressesUserInfo;
import com.example.minitaxiandroid.entities.userinfo.FavouriteDriverUserInfo;
import com.example.minitaxiandroid.fragments.SearchAddressOnMapFragment;
import com.example.minitaxiandroid.fragments.SearchAddressesFragment;
import com.example.minitaxiandroid.retrofit.MiniTaxiApi;
import com.example.minitaxiandroid.retrofit.RetrofitService;
import com.example.minitaxiandroid.services.UserLocationService;
import com.example.minitaxiandroid.services.UserLoginInfoService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.*;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback {


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private FragmentContainerView fragmentContainerView;
    private GoogleMap mMap;
    private Fragment fragment;
    private SupportMapFragment mapFragment;
    private LocationManager locationManager;
    private UserLocationService locationService;
    private boolean isMapFragment = false;
    private Marker markerForAddressPick;
    private ArrayList<String> favouriteAddressesList;
    private List<FavouriteDriverUserInfo> favouriteDriverUserInfoList;
    private DatabaseReference databaseReference;
    private static Map<Marker, DriverInfo> driverMarkerMap;
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
        setFragment();
        UserLoginInfoService.init(MainActivity.this);
        //TODO
        UserLoginInfoService.addProperty("isLogin",  "True");
        UserLoginInfoService.addProperty("userId", "1");
        UserLoginInfoService.addProperty("username", "viktor2002");
        UserLoginInfoService.addProperty("password", "driver");
        UserLoginInfoService.addProperty("rankId", "1");
        UserLoginInfoService.init(MainActivity.this);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView header = headerView.findViewById(R.id.headerProfileName);
        header.setText(UserLoginInfoService.getProperty("username"));
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.getView().setClickable(false);
        driverMarkerMap = new HashMap<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://energy-taxi-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();
        getFavouriteDriverUserInfoRequest();
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorId){
        Drawable vectorDrawable= ContextCompat.getDrawable(context, vectorId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap=Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void getFavouriteDriverUserInfoRequest(){
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi favouriteDriversApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        String userId = UserLoginInfoService.getProperty("userId");
        favouriteDriversApi.getFavouriteDriverUserInfo(Integer.valueOf(userId))
                .enqueue(new Callback<List<FavouriteDriverUserInfo>>() {
                    @Override
                    public void onResponse(Call<List<FavouriteDriverUserInfo>> call, Response<List<FavouriteDriverUserInfo>> response) {
                        favouriteDriverUserInfoList = response.body();
                        getDriverLocation();
                        setDriverLocationListener();
                        getFavouritesAddressesRequest();
                    }

                    @Override
                    public void onFailure(Call<List<FavouriteDriverUserInfo>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Failed to load favourite drivers info",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getDriverLocation(){
        DatabaseReference uidRef = databaseReference.child("drivers-info");
        Query query1 = uidRef.orderByChild("status").equalTo("IN_ORDER");
        Query query2 = uidRef.orderByChild("status").equalTo("ONLINE");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DriverInfo driverInfo = dataSnapshot.getValue(DriverInfo.class);
                    Log.d("TAG", driverInfo.getDriverFirstName());
                    setDriverOnMap(driverInfo);
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        };
        query1.addListenerForSingleValueEvent(valueEventListener);
        query2.addListenerForSingleValueEvent(valueEventListener);
    }
    private void setDriverLocationListener(){
        DatabaseReference uidRef = databaseReference.child("drivers-info");
        uidRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
//                DriverInfo driverInfo = snapshot.getValue(DriverInfo.class);
//                Log.d("TAG", driverInfo.getDriverFirstName());
//                setDriverOnMap(driverInfo);
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                DriverInfo driverInfo = snapshot.getValue(DriverInfo.class);
                Log.d("TAG", driverInfo.getDriverFirstName());
                if(!driverInfo.getStatus().equals(DRIVER_STATUS.OFFLINE)) {
                    setDriverOnMap(driverInfo);
                }
                else {
                    removeDriverMarker(driverInfo);
                }
            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


    private void setDriverOnMap(DriverInfo driverInfo){
        System.out.println("add marker");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boolean isFavourite = checkIsFavouriteDriver(driverInfo);
                addMarker(driverInfo, isFavourite);
            }
        });
    }

    private boolean checkIsFavouriteDriver(DriverInfo driverInfo){
        for(FavouriteDriverUserInfo favouriteDriverUserInfo: favouriteDriverUserInfoList){
            if(favouriteDriverUserInfo.getDriverId() == driverInfo.getDriverId()){
                return true;
            }
        }
        return false;
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

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng newLatLng = new LatLng(UserLocationService.DEFAULT_LOCATION.latitude,
                UserLocationService.DEFAULT_LOCATION.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(newLatLng));
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {

            }
        });
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {

                return false;
            }
        });
        mMap.setOnMapClickListener(latLng -> {
            if (mapFragment.getView().isClickable()){
                addAddressMarker(latLng);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                setFragment();
                addText(getAddressName(latLng));
                if(checkIsFavourite(getAddressName(latLng))){
                    setLike();
                }
                else{
                    setLikeVisible();
                }
            }
        });
        mMap.setOnMarkerClickListener(marker -> {
            if(driverMarkerMap.containsKey(marker)){
                DriverInfo driverInfo = driverMarkerMap.get(marker);
                if(checkIsFavouriteDriver(driverInfo)) {
                    setFavouriteDriver(driverInfo);
                }
            }
            return false;
        });
    }

    private boolean checkIsFavourite(String address){
        for(String favouriteAddress: favouriteAddressesList){
            if(favouriteAddress.equals(address)){
                return true;
            }
        }
        return false;
    }

    private void setFavouriteDriver(DriverInfo driverInfo){
        if (fragment instanceof SearchAddressesFragment){
            SearchAddressesFragment searchAddressesFragment = (SearchAddressesFragment)fragment;
            searchAddressesFragment.setFavouriteDriver(driverInfo);
        }
        else {
            Log.d("fragment not SearchAddressOnMapFragment",
                    "in setLike fragment not SearchAddressOnMapFragment");
        }
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
            addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
            address = addresses.get(0).getAddressLine(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    public void addAddressMarker(LatLng latLng){
        if(markerForAddressPick!=null){
            markerForAddressPick.remove();
        }
        markerForAddressPick = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
    }

    public void addMarker(DriverInfo driverInfo, boolean isFavourite){
        LatLng newLatLng = new LatLng(driverInfo.getLatitude(), driverInfo.getLongitude());
        String driverName = driverInfo.getDriverFirstName() + " " + driverInfo.getDriverSurName();
        if(!driverMarkerMap.isEmpty()){
            removeDriverMarker(driverInfo);
        }
        if(driverInfo.getStatus().equals(DRIVER_STATUS.IN_ORDER)){
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(newLatLng)
                    .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.in_order_car)));
            driverMarkerMap.put(marker, driverInfo);
        }
        else{
            if(isFavourite){
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(newLatLng)
                        .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.favourite_car))
                        .title(driverName));
                driverMarkerMap.put(marker, driverInfo);
            }
            else{
                if(driverInfo.getStatus().equals(DRIVER_STATUS.ONLINE)){
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(newLatLng)
                            .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.default_car)));
                    driverMarkerMap.put(marker, driverInfo);
                }
            }
        }
    }

    public static Marker getKeyByValue(DriverInfo value) {
        for (Map.Entry<Marker, DriverInfo> entry : driverMarkerMap.entrySet()) {
            if (entry.getValue().getDriverId() == value.getDriverId()) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void removeDriverMarker(DriverInfo driverInfo){
        Marker marker = getKeyByValue(driverInfo);
        if(marker!=null){
            marker.remove();
            driverMarkerMap.remove(marker);
        }
    }

    public void removeMarker(){
        if(markerForAddressPick!=null){
            markerForAddressPick.remove();
        }
    }

    public void setMapMarkerVisible(boolean value){
        mapFragment.getView().setClickable(value);
    }

    public void getFavouritesAddressesRequest(){
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi favouriteAddressApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        favouriteAddressApi.getFavouriteAddressesUserInfo(Integer.valueOf(UserLoginInfoService.getProperty("userId")))
                .enqueue(new Callback<List<FavouriteAddressesUserInfo>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<FavouriteAddressesUserInfo>> call,
                                           @NotNull Response<List<FavouriteAddressesUserInfo>> response) {
                        assert response.body() != null;
                        parseFavouriteAddressesList(response.body());
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<FavouriteAddressesUserInfo>> call, @NotNull Throwable t) {
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
}
