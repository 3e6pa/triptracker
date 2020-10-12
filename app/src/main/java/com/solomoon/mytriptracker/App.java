package com.solomoon.mytriptracker;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import androidx.room.Room;

import com.solomoon.mytriptracker.core.DefaultTripManager;
import com.solomoon.mytriptracker.data.AppDatabase;
import com.solomoon.mytriptracker.data.RoomMigration;
import com.solomoon.mytriptracker.service.GeolocationService;

public class App extends Application implements ServiceConnection {

    private GeolocationService geolocationService;

    private AppDatabase database;

    private DefaultTripManager tripManager;

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.instance = this;

        database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "TripTracker-db")
                .allowMainThreadQueries()
                .addMigrations(RoomMigration.MIGRATION_1_2)
                .build();

        tripManager = new DefaultTripManager(database);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        GeolocationService.LocalBinder localBinder = (GeolocationService.LocalBinder) service;
        geolocationService = localBinder.getService();
        geolocationService.setDatabase(database);

        startTracking();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        geolocationService = null;
    }

    private void startTracking(){
        if(tripManager.getActiveTrip() != null){
            geolocationService.startLocationListener();;
        }
    }

    public GeolocationService getGeolocationService() {
        return this.geolocationService;
    }

    public void doBindService() {
        bindService(new Intent(this, GeolocationService.class), this, BIND_AUTO_CREATE);
    }

    public AppDatabase getDatabase() {
        return database;
    }


}
