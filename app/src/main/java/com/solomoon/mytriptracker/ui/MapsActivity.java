package com.solomoon.mytriptracker.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.SortedList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Observable;
import android.location.Location;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.solomoon.mytriptracker.App;
import com.solomoon.mytriptracker.R;
import com.solomoon.mytriptracker.core.DefaultTripManager;
import com.solomoon.mytriptracker.core.DefaultTripPointManager;
import com.solomoon.mytriptracker.exceptions.TripNotFoundException;
import com.solomoon.mytriptracker.models.Trip;
import com.solomoon.mytriptracker.models.TripPoint;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String EXTRA_TRIP_ID = "MapsActivity.TripId";

    private static final int DEFAULT_MAP_ZOOM = 14;

    private SortedList<TripPoint> tripPointSortedList;

    private GoogleMap mMap;
    private List<Marker> mapMarkers;
    private Button btnStopTrip;

    private DefaultTripManager tripManager;
    private DefaultTripPointManager tripPointManager;
    private String tripId;

    private static void clearMarkers(List<Marker> mapMarkers) {
        if (mapMarkers != null)
            for (Marker marker : mapMarkers) {
                marker.remove();
            }
        mapMarkers.clear();
    }

    public static void start(Activity caller, String tripId) {
        Intent intent = new Intent(caller, MapsActivity.class);
        intent.putExtra(EXTRA_TRIP_ID, tripId);
        caller.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tripManager = new DefaultTripManager(App.getInstance().getDatabase());
        tripPointManager = new DefaultTripPointManager(App.getInstance().getDatabase());

        if (getIntent().hasExtra(EXTRA_TRIP_ID)) {
            tripId = getIntent().getStringExtra(EXTRA_TRIP_ID);
        }

        mapMarkers = new ArrayList<>();

        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnStopTrip = findViewById(R.id.btnStopTrip);
        btnStopTrip.setOnClickListener(v -> {
            if (App.getInstance() != null) {
                tripManager.endTrip();
                finish();
            }
        });

        try{
            setVisibleBtnStopTrip(tripManager.checkActiveTrip(tripId));
        }catch (TripNotFoundException e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void setVisibleBtnStopTrip(boolean visible){
        if(visible){
            btnStopTrip.setVisibility(View.VISIBLE);
        } else {
            btnStopTrip.setVisibility(View.INVISIBLE);
        }
    }


    public void setMarker(Location location) {
        clearMarkers(mapMarkers);
        if ((mMap != null) && (location != null)) {
            LatLng currentPosiotion = new LatLng(location.getLatitude(), location.getLongitude());
            mapMarkers.add(mMap.addMarker(new MarkerOptions().position(currentPosiotion).title("Current position")));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosiotion));
        }

    }

    private void drawDirections(TripPoint tripPoint1, TripPoint tripPoint2) {
        mMap.addPolyline(new PolylineOptions()
                .clickable(false)
                .add(new LatLng(tripPoint1.getLatitude(), tripPoint1.getLongitude())
                    ,new LatLng(tripPoint2.getLatitude(), tripPoint2.getLongitude())));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(tripPoint2.getLatitude(), tripPoint2.getLongitude()), DEFAULT_MAP_ZOOM));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        tripPointSortedList = new SortedList<>(TripPoint.class, new SortedList.Callback<TripPoint>() {
            @Override
            public int compare(TripPoint o1, TripPoint o2) {
                if ((o2.getTimestamp() == o1.getTimestamp())) {
                    return 0;
                } else
                    return -1;
            }

            @Override
            public void onChanged(int position, int count) {
            }

            @Override
            public boolean areContentsTheSame(TripPoint oldItem, TripPoint newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(TripPoint item1, TripPoint item2) {
                return item1.getTimestamp() == item2.getTimestamp();
            }

            @Override
            public void onInserted(int position, int count) {
                if (count>1){
                    drawDirections(tripPointSortedList.get(1),tripPointSortedList.get(0));
                }
            }

            @Override
            public void onRemoved(int position, int count) {
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
            }
        });

        LiveData<List<TripPoint>> tripPoints = tripPointManager.getTripPointByTripId(tripId);
        tripPoints.observe(this, tripPoints1 -> {
            tripPointSortedList.replaceAll(tripPoints1);
        });

    }

}
