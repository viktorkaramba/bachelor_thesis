package com.example.minitaxiandroid.services;

import android.location.Location;
import android.location.LocationListener;
import android.util.Log;
import androidx.annotation.NonNull;
import com.example.minitaxiandroid.entities.document.DRIVER_STATUS;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class DriverLocationService implements LocationListener {

    private LatLng currentLocation;

    private int driverId;

    private DRIVER_STATUS driverStatus;

    public void setDriverStatus(DRIVER_STATUS driverStatus) {
        this.driverStatus = driverStatus;
    }

    private DatabaseReference databaseReference;
    public DriverLocationService(int driverId, DRIVER_STATUS driverStatus, DatabaseReference databaseReference){
        this.driverId = driverId;
        this.driverStatus = driverStatus;
        this.databaseReference = databaseReference;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if(driverStatus.equals(DRIVER_STATUS.ONLINE)){
            String id = "driver-" + driverId;
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("drivers-info/"+ id +"/latitude", location.getLatitude());
            childUpdates.put("drivers-info/"+ id +"/longitude",  location.getLongitude());
            databaseReference.updateChildren(childUpdates).addOnSuccessListener(aVoid -> {
                Log.d("TAG DRIVER LOCATION", "Driver location successfully changed");
            }).addOnFailureListener(e -> {
                Log.d("TAG DRIVER LOCATION", "Error to change driver location");
            });
        }
    }

    public LatLng getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(LatLng currentLocation) {
        this.currentLocation = currentLocation;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public void setDatabaseReference(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }
}
