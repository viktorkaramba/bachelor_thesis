package com.example.minitaxiandroid.constants;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class DistanceConstants {
    public final static float MIN_DISTANCE = 1;
    public final static float MAX_DISTANCE = 30;

    public static final LatLngBounds NETISHYN_BOUNDS = new LatLngBounds(
            new LatLng(50.0697, 26.1612),  new LatLng(50.7382, 28.1492));

}
