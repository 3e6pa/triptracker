package com.solomoon.mytriptracker.core;

import android.location.Location;

import androidx.lifecycle.LiveData;

import com.solomoon.mytriptracker.App;
import com.solomoon.mytriptracker.data.AppDatabase;
import com.solomoon.mytriptracker.data.TripDao;
import com.solomoon.mytriptracker.data.TripPointDao;
import com.solomoon.mytriptracker.models.Trip;
import com.solomoon.mytriptracker.models.TripPoint;
import com.solomoon.mytriptracker.service.GeolocationService;

public class DefaultTripManager {

    private TripDao tripDao;
    private TripPointDao tripPointDao;
    private GeolocationService geolocationService;

    public DefaultTripManager(AppDatabase appDatabase){
        tripDao = appDatabase.tripDao();
        tripPointDao = appDatabase.tripPointDao();
        geolocationService = App.getInstance().getGeolocationService();
    }

    public Trip getActiveTrip(){
        return tripDao.getActiveTrip();
    }

    public void startNewTrip(String tripName, String userId){
        if (getActiveTrip()==null){
            Trip newTrip = new Trip();
            newTrip.setName(tripName);
            newTrip.setUserId(userId);
            newTrip.setSartTimestamp(System.currentTimeMillis());
            tripDao.insert(newTrip);

            geolocationService.startLocationListener();
        }
    }

    public void endTrip(){
        Trip activeTrip = getActiveTrip();
        if (activeTrip != null){
            activeTrip.setEndTimestamp(System.currentTimeMillis());
            tripDao.update(activeTrip);
        }
    }

//    public

    public void setTripPointToActiveTrip(Location location){
        Trip currentTrip = getActiveTrip();
        if(currentTrip != null){
            TripPoint newTripPoint = new TripPoint();
            newTripPoint.setTripId(currentTrip.getId());
            newTripPoint.setLatitude(location.getLatitude());
            newTripPoint.setLongitude(location.getLongitude());
            newTripPoint.setTimestamp(System.currentTimeMillis());
            tripPointDao.insert(newTripPoint);

            geolocationService.stopLocationListener();
        }
    }

}
