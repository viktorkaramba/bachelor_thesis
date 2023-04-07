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

    //
//    private LocationManager locationManager;

//    private static final LatLng newLatLngList[] = new LatLng[]{
//            new LatLng(50.334113140157406, 26.6505691638537),
//            new LatLng(50.335004945026725, 26.648851438008776),
//            new LatLng(50.334385012813804, 26.64632037679038),
//            new LatLng(50.334065650638074, 26.64293581818439)
//    };

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d("mylog", "Got User Location: " + location.getLatitude() + ", " + location.getLongitude());
        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
    }
}
