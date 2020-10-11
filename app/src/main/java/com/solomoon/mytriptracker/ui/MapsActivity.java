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
import com.solomoon.mytriptracker.core.DefaultTripManager;
import com.solomoon.mytriptracker.models.TripPoint;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String EXTRA_TRIP_ID = "MapsActivity.TripId";

    private GoogleMap mMap;
    private List<Marker> mapMarkers;

    private DefaultTripManager tripManager;

    private static void clearMarkers(List<Marker> mapMarkers) {
        if (mapMarkers != null)
            for (Marker marker : mapMarkers) {
                marker.remove();
            }
        mapMarkers.clear();
    }

    public static void start(Activity caller, String tripId) {
        Intent intent = new Intent(caller, MapsActivity.class);
        intent.putExtra(EXTRA_TRIP_ID,tripId);
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tripManager = new DefaultTripManager(App.getInstance().getDatabase());

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

    private void drawDirections(List<TripPoint> tripPoints){

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
