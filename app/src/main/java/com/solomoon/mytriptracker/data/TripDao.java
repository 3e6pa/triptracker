package com.solomoon.mytriptracker.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.solomoon.mytriptracker.model.Trip;

import java.util.List;

@Dao
public interface TripDao {

    @Query("SELECT * FROM Trip")
    List<Trip> getAll();

    @Query("SELECT * FROM Trip")
    LiveData<List<Trip>> getAllLiveData();

    @Query("SELECT * FROM Trip WHERE id = :id LIMIT 1")
    Trip getTripById(String id);

    @Query("SELECT * FROM Trip WHERE endTimestamp = NULL")
    Trip getActiveTrip();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Trip trip);

    @Update
    void update(Trip trip);

    @Delete
    void delete(Trip trip);

}
