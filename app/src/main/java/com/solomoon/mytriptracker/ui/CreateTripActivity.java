package com.solomoon.mytriptracker.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.solomoon.mytriptracker.App;
import com.solomoon.mytriptracker.R;
import com.solomoon.mytriptracker.core.DefaultAppSettingsManager;
import com.solomoon.mytriptracker.core.DefaultTripManager;
import com.solomoon.mytriptracker.models.Trip;

public class CreateTripActivity extends AppCompatActivity {

    public static void start(Activity caller) {
        Intent intent = new Intent(caller, CreateTripActivity.class);
        caller.startActivity(intent);
    }

    DefaultTripManager tripManager;
    DefaultAppSettingsManager appSettingsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);

        tripManager = new DefaultTripManager(App.getInstance().getDatabase());
        appSettingsManager = new DefaultAppSettingsManager(App.getInstance().getDatabase());

        findViewById(R.id.btnStartNewTrip).setOnClickListener(v -> {
            startNewTrip();
        });
    }

    private void startNewTrip() {
        EditText edtTripName = findViewById(R.id.edtTripName);
        if (TextUtils.isEmpty(edtTripName.getText().toString())) {
            Toast.makeText(this, R.string.empty_trip_name, Toast.LENGTH_SHORT).show();
            return;
        }
        String tripName = edtTripName.getText().toString().trim();
        String userId = appSettingsManager.getCurrentUserId();
        tripManager.startNewTrip(tripName,userId);

        Trip activeTrip = tripManager.getActiveTrip();
        MapsActivity.start(this, activeTrip.getId());
        finish();
    }
}
