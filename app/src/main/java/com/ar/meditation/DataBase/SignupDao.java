package com.ar.meditation.DataBase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ar.meditation.model.SignupModels;

import java.util.List;

@Dao
public interface SignupDao {
    @Insert
    void insert(SignupModels signupModel);

    @Query("SELECT * FROM signup_table WHERE cnic LIKE :cnic")
    LiveData<List<SignupModels>> getSignupByCnic(String cnic);

    @Query("SELECT * FROM signup_table WHERE cnic LIKE :cnic AND pass LIKE :pass")
    LiveData<List<SignupModels>> getSignupByCnicAndPass(String cnic, String pass);
}
