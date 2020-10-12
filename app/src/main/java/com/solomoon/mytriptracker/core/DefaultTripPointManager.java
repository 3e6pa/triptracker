package com.solomoon.mytriptracker.core;

import androidx.lifecycle.LiveData;

import com.solomoon.mytriptracker.data.AppDatabase;
import com.solomoon.mytriptracker.data.TripPointDao;
import com.solomoon.mytriptracker.models.TripPoint;

import java.util.List;

public class DefaultTripPointManager {

    private TripPointDao tripPointDao;

    public DefaultTripPointManager(AppDatabase appDatabase) {
        tripPointDao = appDatabase.tripPointDao();
    }

    public LiveData<List<TripPoint>> getTripPointByTripId(String tripId) {
        return tripPointDao.getTripPointByTripId(tripId);
    }
}
