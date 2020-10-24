package com.t3h.service.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.t3h.service.model.User;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class UserDatabase extends RoomDatabase {
    public abstract UserDao getUserDao();

    public static UserDatabase getUserDatabase(Context context) {
        return Room.databaseBuilder(
                context, UserDatabase.class, "user.db")
                .allowMainThreadQueries()//FIXME remove on production, this is only for test
                .fallbackToDestructiveMigration()
                .build();
    }
}
