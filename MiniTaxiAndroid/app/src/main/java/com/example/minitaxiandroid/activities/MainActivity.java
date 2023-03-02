package com.example.minitaxiandroid.activities;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.minitaxiandroid.R;
import com.example.minitaxiandroid.services.LocationService;
import com.example.minitaxiandroid.services.UserLoginInfoService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private GoogleMap mMap;
    private Marker markerName;
    private LocationManager locationManager;
    private LocationService locationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_views);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle  = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        UserLoginInfoService.init(MainActivity.this);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView header = headerView.findViewById(R.id.headerProfileName);
        header.setText(UserLoginInfoService.getProperty("username"));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationService = new LocationService(locationManager);
        locationService.doRequest();
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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng newLatLng = new LatLng(locationService.getCurrentLocation().latitude,
                locationService.getCurrentLocation().longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(newLatLng));
        addDrivers();
    }


    private void addDrivers() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            public void run() {
//                markerName.remove();
                LatLng newLatLng = new LatLng(locationService.getCurrentLocation().latitude,
                        locationService.getCurrentLocation().longitude);
                markerName = mMap.addMarker(new MarkerOptions()
                        .position(newLatLng)
                        .title("Marker"));
                System.out.println("Move");
                handler.postDelayed(this, 2000);
            }
        }, 0);
    }
}
