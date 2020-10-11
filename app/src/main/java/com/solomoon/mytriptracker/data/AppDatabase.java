package com.solomoon.mytriptracker.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.solomoon.mytriptracker.model.AppSettings;
import com.solomoon.mytriptracker.model.Trip;
import com.solomoon.mytriptracker.model.TripPoint;
import com.solomoon.mytriptracker.model.User;

@Database(entities = {User.class, Trip.class, TripPoint.class, AppSettings.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract TripDao tripDao();

    public abstract TripPointDao tripPointDao();

    public abstract AppSettingsDao appSettingsDao();

}
