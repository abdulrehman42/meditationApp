package com.ar.meditation.DataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ar.meditation.model.SignupModels;

@Database(entities = {SignupModels.class}, version = 1, exportSchema = false)
public abstract class SignupDatabase extends RoomDatabase {
    public abstract SignupDao signupDao();

    private static volatile SignupDatabase INSTANCE;

    public static SignupDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (SignupDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            SignupDatabase.class,
                            "signup_database"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
