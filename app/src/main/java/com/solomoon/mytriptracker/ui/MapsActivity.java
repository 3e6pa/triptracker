package com.solomoon.mytriptracker.ui;

import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.solomoon.mytriptracker.App;
import com.solomoon.mytriptracker.R;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Marker> mapMarkers;

    private static void clearMarkers(List<Marker> mapMarkers) {
        if (mapMarkers != null)
            for (Marker marker : mapMarkers) {
                marker.remove();
            }
        mapMarkers.clear();
    }

    public static void start(Activity caller) {
        Intent intent = new Intent(caller, MapsActivity.class);
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mapMarkers = new ArrayList<>();

        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        findViewById(R.id.moveToCurrentPosition).setOnClickListener(v -> {
            if (App.getInstance() != null){
               setMarker(App.getInstance().getGeolocationService().getLocation());
            }
        });

    }


    public void setMarker(Location location) {
        clearMarkers(mapMarkers);
        if ((mMap != null) && (location != null)) {
            LatLng currentPosiotion = new LatLng(location.getLatitude(), location.getLongitude());
            mapMarkers.add(mMap.addMarker(new MarkerOptions().position(currentPosiotion).title("Current position")));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosiotion));
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
