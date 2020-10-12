package com.solomoon.mytriptracker.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.solomoon.mytriptracker.models.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM User WHERE id = :userId LIMIT 1")
    User getUserById(String userId);

    @Query("SELECT * FROM User WHERE login = :login ")
    User getUserByLogin(String login);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);
}
