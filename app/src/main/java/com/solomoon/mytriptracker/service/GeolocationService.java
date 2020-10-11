package com.solomoon.mytriptracker.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.StringJoiner;

public class GeolocationService extends Service implements LocationListener {

    public class LocalBinder extends Binder {
        public GeolocationService getService() {
            return GeolocationService.this;
        }
    }

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 1 meters

    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    boolean isGPSEnabled = false;

    boolean isNetworkEnabled = false;
    private Location location;

    public GeolocationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationListener();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startLocationListener();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        Toast.makeText(this, location.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private LocationManager getLocationManager() {
        return (LocationManager) this.getSystemService(LOCATION_SERVICE);
    }

    public void stopLocationListener() {
        LocationManager locationManager = getLocationManager();

        if (locationManager != null) {
            try {
                locationManager.removeUpdates(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void startLocationListener() {
        LocationManager locationManager = getLocationManager();

        if (locationManager != null) {
            try {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    isGPSEnabled = locationManager
                            .isProviderEnabled(LocationManager.GPS_PROVIDER);
                    isNetworkEnabled = locationManager
                            .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    }
                    if (isGPSEnabled) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Location getLocation(){
        return this.location;
    }

}
