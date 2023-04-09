package com.example.minitaxiandroid.services;

import android.location.Location;
import android.location.LocationListener;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.maps.model.LatLng;

public class UserLocationService implements LocationListener {

    private LatLng currentLocation;
    public final static LatLng DEFAULT_LOCATION = new LatLng(50.333798206435915, 26.65000580334267);

    public LatLng getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(LatLng currentLocation) {
        this.currentLocation = currentLocation;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d("mylog", "Got User Location: " + location.getLatitude() + ", " + location.getLongitude());
        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
    }
}
