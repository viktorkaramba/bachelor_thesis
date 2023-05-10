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
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import com.example.minitaxiandroid.R;
import com.example.minitaxiandroid.api.MiniTaxiApi;
import com.example.minitaxiandroid.api.RetrofitService;
import com.example.minitaxiandroid.entities.auth.RegisterResponse;
import com.example.minitaxiandroid.entities.document.DRIVER_STATUS;
import com.example.minitaxiandroid.entities.userinfo.DriverInfo;
import com.example.minitaxiandroid.entities.userinfo.FavouriteAddressesUserInfo;
import com.example.minitaxiandroid.entities.userinfo.FavouriteDriverUserInfo;
import com.example.minitaxiandroid.fragments.SearchAddressOnMapFragment;
import com.example.minitaxiandroid.fragments.SearchAddressesFragment;
import com.example.minitaxiandroid.services.UserInfoService;
import com.example.minitaxiandroid.services.UserLocationService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.*;

import static com.example.minitaxiandroid.constants.DistanceConstants.NETISHYN_BOUNDS;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback {


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ImageView logoutToolbarImageView;
    private FragmentContainerView fragmentContainerView;
    private GoogleMap mMap;
    private Fragment fragment;
    private SupportMapFragment mapFragment;
    private LocationManager locationManager;
    private UserLocationService userLocationService;
    private boolean isMapFragment = false;
    private Marker markerForAddressPick;
    private ArrayList<String> favouriteAddressesList;
    private List<FavouriteDriverUserInfo> favouriteDriverUserInfoList;
    private DatabaseReference databaseReference;
    private static Map<Marker, DriverInfo> driverMarkerMap;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button yesGpsPermissionButton, noGpsPermissionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_views);
        toolbar = findViewById(R.id.mainToolbar);
        logoutToolbarImageView = findViewById(R.id.logoutToolbarImageView);
        logoutToolbarImageView.setOnClickListener(view -> logout());
        fragmentContainerView = findViewById(R.id.fragmentContainerView);
        UserInfoService.init(MainActivity.this);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        setFragment();
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView header = headerView.findViewById(R.id.headerProfileName);
        header.setText(UserInfoService.getProperty("username"));
        FloatingActionButton userProfilefloatingActionButton =  headerView.findViewById(R.id.userProfileButton);
        userProfilefloatingActionButton.setOnClickListener(view -> goUserProfile());
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.getView().setClickable(false);
        driverMarkerMap = new HashMap<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://energy-taxi-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();
        getFavouriteDriverUserInfoRequest();
    }

    private void logout() {
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi api = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        api.logout("Bearer " + UserInfoService.getProperty("access_token"))
                .enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<RegisterResponse> call,
                                           @NotNull Response<RegisterResponse> response) {
                        System.out.println("logout: " + response.body());
                        if(response.body()!=null){
                            try {
                                if(response.errorBody() != null){
                                    if(response.errorBody().string().contains(getResources()
                                            .getString(R.string.token_expired))){
                                        refreshToken();
                                    }
                                }
                                else{
                                    UserInfoService.clear();
                                    goLogin();
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<RegisterResponse> call, Throwable t) {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(MainActivity.this, "Failed to logout",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
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
        String userId = UserInfoService.getProperty("userId");
        favouriteDriversApi.getFavouriteDriverUserInfo("Bearer " + UserInfoService.getProperty("access_token"),
                        Integer.valueOf(userId))
                .enqueue(new Callback<List<FavouriteDriverUserInfo>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<FavouriteDriverUserInfo>> call,
                                           @NotNull Response<List<FavouriteDriverUserInfo>> response) {
                        if(response.body() != null){
                            try {
                                if(response.errorBody() != null){
                                    if(response.errorBody().string().contains(getResources()
                                            .getString(R.string.token_expired))){
                                        refreshToken();
                                    }
                                }
                                else{
                                    favouriteDriverUserInfoList = response.body();
                                    System.out.println(favouriteDriverUserInfoList);
                                    getDriverLocation();
                                    setDriverLocationListener();
                                    getFavouritesAddressesRequest();
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
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

            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                DriverInfo driverInfo = snapshot.getValue(DriverInfo.class);
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
        runOnUiThread(() -> {
            boolean isFavourite = checkIsFavouriteDriver(driverInfo);
            addDriverMarker(driverInfo, isFavourite);
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
            case R.id.nav_help:
                Intent help_intent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(help_intent);
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
        mMap.setMyLocationEnabled(true);
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                this, R.raw.map_style_json));
        mMap.setLatLngBoundsForCameraTarget(NETISHYN_BOUNDS);
        mMap.setMinZoomPreference(10);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        userLocationService = new UserLocationService();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 15,
                userLocationService);
        boolean isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(isGPSEnable){
            locationManager.getCurrentLocation(
                    LocationManager.GPS_PROVIDER,
                    null,
                    getMainExecutor(),
                    location -> {
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        userLocationService.setCurrentLocation(latLng);
                        setAddressUserInSearchForm(getAddressName(latLng));
                    });
        }
        else{
            LatLng newLatLng = new LatLng(UserLocationService.DEFAULT_LOCATION.latitude,
                    UserLocationService.DEFAULT_LOCATION.longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(newLatLng));
        }
        mMap.setOnMyLocationButtonClickListener(() -> {
            boolean isEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if(!isEnable){
                showGPSPDisableDialog();
            }
            else{
                if (mapFragment.getView().isClickable()) {
                    locationManager.getCurrentLocation(
                            LocationManager.GPS_PROVIDER,
                            null,
                            getMainExecutor(),
                            location -> {
                                LatLng latLng = new LatLng(userLocationService.getCurrentLocation().latitude,
                                        userLocationService.getCurrentLocation().longitude);
                                addText(getAddressName(latLng));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                setFragment();
                                if (checkIsFavourite(getAddressName(latLng))) {
                                    setLike();
                                } else {
                                    setLikeVisible();
                                }
                            });

                }
                else{
                    locationManager.getCurrentLocation(
                            LocationManager.GPS_PROVIDER,
                            null,
                            getMainExecutor(),
                            location -> {
                                LatLng latLng = new LatLng(userLocationService.getCurrentLocation().latitude,
                                        userLocationService.getCurrentLocation().longitude);
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                userLocationService.setCurrentLocation(latLng);
                                setAddressUserInSearchForm(getAddressName(latLng));
                            });
                }
            }
            return false;
        });
        mMap.setOnMapClickListener(latLng -> {
            if (mapFragment.getView().isClickable()){
                setFragment();
                addAddressMarker(latLng);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
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
            if (!mapFragment.getView().isClickable()) {
                if (driverMarkerMap.containsKey(marker)) {
                    DriverInfo driverInfo = driverMarkerMap.get(marker);
                    if (!driverInfo.getStatus().equals(DRIVER_STATUS.IN_ORDER)) {
                        if (checkIsFavouriteDriver(driverInfo)) {
                            setFavouriteDriver(driverInfo);
                        }
                    }
                }
            }
            return false;
        });
    }

    private void showGPSPDisableDialog() {
        dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.gps_permission_popwindow, null);
        initializeGPSDisableDialog(contactPopupView);
        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.card_view_border_reverse, getTheme()));
        dialog.show();
    }

    private void initializeGPSDisableDialog(View contactPopupView) {
        yesGpsPermissionButton = contactPopupView.findViewById(R.id.yesGpsPermissionButton);
        yesGpsPermissionButton.setOnClickListener(view -> turnGPSOn());
        noGpsPermissionButton = contactPopupView.findViewById(R.id.continueGpsPermissionButton);
        noGpsPermissionButton.setText(getResources().getString(R.string.no));
        noGpsPermissionButton.setOnClickListener(view -> {
            runOnUiThread(() -> dialog.cancel());
        });
    }

    private void turnGPSOn(){
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
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

    private void setAddressUserInSearchForm(String address){
        if (fragment instanceof SearchAddressesFragment){
            SearchAddressesFragment searchAddressesFragment = (SearchAddressesFragment)fragment;
            searchAddressesFragment.setAddressUserInSearchForm(address);
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
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
    }

    public void addDriverMarker(DriverInfo driverInfo, boolean isFavourite){
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
        favouriteAddressApi.getFavouriteAddressesUserInfo("Bearer " + UserInfoService.getProperty("access_token"),
                        Integer.valueOf(UserInfoService.getProperty("userId")))
                .enqueue(new Callback<List<FavouriteAddressesUserInfo>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<FavouriteAddressesUserInfo>> call,
                                           @NotNull Response<List<FavouriteAddressesUserInfo>> response) {
                        if(response.body()!=null){
                            try {
                                if(response.errorBody() != null){
                                    if(response.errorBody().string().contains(getResources()
                                            .getString(R.string.token_expired))){
                                        refreshToken();
                                    }
                                }
                                else{
                                    parseFavouriteAddressesList(response.body());
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
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
                                finish();
                                startActivity(getIntent());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        MainActivity.this.runOnUiThread(() ->
                                Toast.makeText(MainActivity.this, "Failed to check user authentication",
                                Toast.LENGTH_SHORT).show());
                    }
                });
    }
    private void goLogin() {
        Intent intent = new Intent(this, UserLoginActivity.class);
        startActivity(intent);
    }
    private void goUserProfile() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

    public void addNewAddressToList(String address){
        favouriteAddressesList.add(address);
        System.out.println(favouriteAddressesList);
    }
}
