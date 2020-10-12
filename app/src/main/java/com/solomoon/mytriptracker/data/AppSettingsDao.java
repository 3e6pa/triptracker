package com.solomoon.mytriptracker.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.solomoon.mytriptracker.models.AppSettings;


@Dao
public interface AppSettingsDao {

    @Query("SELECT * FROM AppSettings LIMIT 1")
    AppSettings getAppSettings();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AppSettings appSettings);

    @Update
    void update(AppSettings appSettings);
}
