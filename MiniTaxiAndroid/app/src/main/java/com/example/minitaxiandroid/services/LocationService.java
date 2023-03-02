package com.example.minitaxiandroid.services;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.maps.model.LatLng;

public class LocationService implements LocationListener {
    private final static int INTERVAL = 2000;
    private static int COUNTER = 0;
    private final static LatLng DEFAULT_LOCATION = new LatLng(50.333798206435915, 26.65000580334267);
    private LocationManager locationManager;
    private LatLng currentLocation;

    private static final LatLng newLatLngList[] = new LatLng[]{
            new LatLng(50.334113140157406, 26.6505691638537),
            new LatLng(50.335004945026725, 26.648851438008776),
            new LatLng(50.334385012813804, 26.64632037679038),
            new LatLng(50.334065650638074, 26.64293581818439)
    };

    public LocationService(LocationManager locationManager){
        this.locationManager = locationManager;
        this.currentLocation = DEFAULT_LOCATION;
    }

    public LatLng getCurrentLocation() {
        return currentLocation;
    }

    public void doRequest() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            public void run() {
                requestLocation(locationManager);
                handler.postDelayed(this, INTERVAL);
            }
        }, 0);
    }

    @SuppressLint("MissingPermission")
    private void requestLocation(LocationManager locationManager) {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 100, this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d("mylog", "Got Location: " + location.getLatitude() + ", " + location.getLongitude());
        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
//        currentLocation = new LatLng(newLatLngList[COUNTER].latitude, newLatLngList[COUNTER].longitude);
//        COUNTER++;
        locationManager.removeUpdates(this);
    }
}
