package com.solomoon.mytriptracker.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.solomoon.mytriptracker.models.TripPoint;

import java.util.List;

@Dao
public interface TripPointDao {
    @Query("SELECT * FROM TripPoint WHERE tripId = :tripId")
    List<TripPoint> getTripPointByTripId(String tripId);

    @Query("SELECT * FROM TripPoint WHERE id = :id LIMIT 1")
    TripPoint getTripPointById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TripPoint tripPoint);

    @Update
    void update(TripPoint tripPoint);

    @Delete
    void delete(TripPoint tripPoint);
}
