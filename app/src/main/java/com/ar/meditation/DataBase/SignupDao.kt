package com.ar.meditation.DataBase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ar.meditation.model.SignupModel

@Dao
interface SignupDao {
    @Insert
    fun insert(signupModel: SignupModel)

    @Query("SELECT *FROM signup_table WHERE cnic LIKE :cnic")
    fun getSignupByCnic(cnic: String): LiveData<List<SignupModel>>

    @Query("SELECT *FROM signup_table WHERE cnic LIKE :cnic AND pass LIKE:pass")
    fun getSignupByCnicAndPass(cnic: String,pass:String):LiveData<List<SignupModel>>
}