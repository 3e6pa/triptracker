package com.solomoon.mytriptracker;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;

import androidx.room.Room;

import com.solomoon.mytriptracker.core.DefaultAppSettingsManager;
import com.solomoon.mytriptracker.core.DefaultUserManager;
import com.solomoon.mytriptracker.data.AppDatabase;
import com.solomoon.mytriptracker.data.RoomMigration;
import com.solomoon.mytriptracker.data.TripDao;
import com.solomoon.mytriptracker.data.TripPointDao;
import com.solomoon.mytriptracker.data.UserDao;
import com.solomoon.mytriptracker.model.AppSettings;
import com.solomoon.mytriptracker.model.User;
import com.solomoon.mytriptracker.service.GeolocationService;
import com.solomoon.mytriptracker.ui.MapsActivity;
import com.solomoon.mytriptracker.ui.TripListActivity;

public class App extends Application implements ServiceConnection {

    private GeolocationService geolocationService;

    private AppDatabase database;

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
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        GeolocationService.LocalBinder localBinder = (GeolocationService.LocalBinder) service;
        geolocationService = localBinder.getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        geolocationService = null;
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
