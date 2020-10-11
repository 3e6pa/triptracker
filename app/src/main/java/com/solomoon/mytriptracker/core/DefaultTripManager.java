package com.solomoon.mytriptracker.core;

import android.location.Location;

import com.solomoon.mytriptracker.data.AppDatabase;
import com.solomoon.mytriptracker.data.TripDao;
import com.solomoon.mytriptracker.data.TripPointDao;
import com.solomoon.mytriptracker.models.Trip;
import com.solomoon.mytriptracker.models.TripPoint;

public class DefaultTripManager {

    private TripDao tripDao;
    private TripPointDao tripPointDao;

    public DefaultTripManager(AppDatabase appDatabase){
        tripDao = appDatabase.tripDao();
        tripPointDao = appDatabase.tripPointDao();
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
        }
    }

    public void endTrip(){
        Trip activeTrip = getActiveTrip();
        if (activeTrip != null){
            activeTrip.setEndTimestamp(System.currentTimeMillis());
            tripDao.update(activeTrip);
        }
    }

    public void setTripPointToActiveTrip(Location location){
        Trip currentTrip = getActiveTrip();
        if(currentTrip != null){
            TripPoint newTripPoint = new TripPoint();
            newTripPoint.setTripId(currentTrip.getId());
            newTripPoint.setLatitude(location.getLatitude());
            newTripPoint.setLongitude(location.getLongitude());
            newTripPoint.setTimestamp(System.currentTimeMillis());
        }
    }


}
