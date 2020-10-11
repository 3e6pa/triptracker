package com.solomoon.mytriptracker;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;

import androidx.room.Room;

import com.solomoon.mytriptracker.data.AppDatabase;
import com.solomoon.mytriptracker.data.TripDao;
import com.solomoon.mytriptracker.data.TripPointDao;
import com.solomoon.mytriptracker.data.UserDao;
import com.solomoon.mytriptracker.service.GeolocationService;

public class App extends Application implements ServiceConnection {

    private GeolocationService geolocationService;

    private AppDatabase database;

    private UserDao userDao;
    private TripDao tripDao;
    private TripPointDao tripPointDao;


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
                .build();

        userDao = database.userDao();
        tripDao = database.tripDao();
        tripPointDao = database.tripPointDao();
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

    public UserDao getUserDao() {
        return userDao;
    }

    public TripDao getTripDao() {
        return tripDao;
    }

    public TripPointDao getTripPointDao() {
        return tripPointDao;
    }



}
