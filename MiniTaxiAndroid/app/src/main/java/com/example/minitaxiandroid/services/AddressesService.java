package com.example.minitaxiandroid.services;

import android.location.Address;
import android.location.Geocoder;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class AddressesService {

    private Geocoder geocoder;

    public AddressesService(Geocoder geocoder){
        this.geocoder = geocoder;
    }

    public List<Address> getAddresses(LatLng latLng, int radius) throws IOException {
        return geocoder.getFromLocation(latLng.latitude, latLng.longitude, radius);
    }
}
