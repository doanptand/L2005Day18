package com.t3h.service.db;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.t3h.service.model.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertUser(User user);

    @Query("SELECT * FROM user")
    List<User> findAllUsers();

    @Query("SELECT * FROM user")
    Cursor findAllUsersAndReturnCursor();

    @Query("SELECT * FROM user WHERE _id = :id")
    Cursor findOneUserAndReturnCursor(int id);
}
