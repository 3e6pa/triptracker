package com.solomoon.mytriptracker.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.solomoon.mytriptracker.App;
import com.solomoon.mytriptracker.R;
import com.solomoon.mytriptracker.models.Trip;
import com.solomoon.mytriptracker.permissions.PermissionManager;
import com.solomoon.mytriptracker.ui.adapters.TripListAdapter;
import com.solomoon.mytriptracker.ui.adapters.TripListViewModel;

import java.util.List;

public class TripListActivity extends PermissionManager  {

    private static final int REQUEST_PERMISSION = 10;

    String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private RecyclerView tripListRecycler;

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

        tripListRecycler = findViewById(R.id.tripList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        tripListRecycler.setLayoutManager(linearLayoutManager);
        tripListRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        final TripListAdapter tripListAdapter = new TripListAdapter();
        tripListRecycler.setAdapter(tripListAdapter);

        TripListViewModel tripListViewModel = ViewModelProviders.of(this).get(TripListViewModel.class);
        tripListViewModel.getNoteLiveData().observe(this, notes -> tripListAdapter.setItems(notes));

        findViewById(R.id.stat_new_trip).setOnClickListener(v -> {
            CreateTripActivity.start(this);
        });
    }


    @Override
    public void onPermissionsGranted(int requestCode) {
        if (requestCode == REQUEST_PERMISSION) {
            if (App.getInstance() != null){
                App.getInstance().doBindService();
            }
        }
    }

    private void setPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            requestAppPermissions(permissions, R.string.permission, REQUEST_PERMISSION);
    }

}
