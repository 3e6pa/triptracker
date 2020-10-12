package com.solomoon.mytriptracker.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.solomoon.mytriptracker.App;
import com.solomoon.mytriptracker.R;
import com.solomoon.mytriptracker.core.DefaultTripManager;
import com.solomoon.mytriptracker.permissions.PermissionManager;
import com.solomoon.mytriptracker.ui.adapters.TripListAdapter;
import com.solomoon.mytriptracker.ui.adapters.TripListViewModel;

public class TripListActivity extends PermissionManager {

    private static final int REQUEST_PERMISSION = 10;

    private String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private RecyclerView tripListRecycler;
    private Button btnCreateTrip;

    private DefaultTripManager tripManager;

    public static void start(Activity caller) {
        Intent intent = new Intent(caller, TripListActivity.class);
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setPermissions();

        setContentView(R.layout.activity_trip_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.title_activity_trip_list);

        tripManager = new DefaultTripManager(App.getInstance().getDatabase());

        initializationTripAdapter();

        btnCreateTrip = findViewById(R.id.stat_new_trip);
        btnCreateTrip.setOnClickListener(v -> {
            CreateTripActivity.start(this);
        });

        setVisibleBtnCreateTrip(tripManager.getActiveTrip() == null);

    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        if (requestCode == REQUEST_PERMISSION) {
            if (App.getInstance() != null) {
                App.getInstance().doBindService();
            }
        }
    }

    private void initializationTripAdapter() {
        tripListRecycler = findViewById(R.id.tripList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        tripListRecycler.setLayoutManager(linearLayoutManager);
        tripListRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        final TripListAdapter tripListAdapter = new TripListAdapter();
        tripListRecycler.setAdapter(tripListAdapter);

        TripListViewModel tripListViewModel = ViewModelProviders.of(this).get(TripListViewModel.class);
        tripListViewModel.getNoteLiveData().observe(this, notes -> tripListAdapter.setItems(notes));
    }

    private void setVisibleBtnCreateTrip(boolean visible) {
        if (visible) {
            btnCreateTrip.setVisibility(View.VISIBLE);
        } else {
            btnCreateTrip.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVisibleBtnCreateTrip(tripManager.getActiveTrip() == null);
    }

    private void setPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            requestAppPermissions(permissions, R.string.permission, REQUEST_PERMISSION);
    }

}
