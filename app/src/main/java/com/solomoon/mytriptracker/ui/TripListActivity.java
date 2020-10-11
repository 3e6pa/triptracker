package com.solomoon.mytriptracker.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.appcompat.widget.Toolbar;

import com.solomoon.mytriptracker.App;
import com.solomoon.mytriptracker.R;
import com.solomoon.mytriptracker.permission.PermissionManager;

public class TripListActivity extends PermissionManager  {

    private static final int REQUEST_PERMISSION = 10;

    String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    public static void start(Activity caller) {
        Intent intent = new Intent(caller, MapsActivity.class);
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

//        findViewById(R.id.stat_new_trip).setOnClickListener(v -> {
//            App.getInstance().getGeolocationService().startLocationListener();
//            MapsActivity.start(this);
//        });
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
