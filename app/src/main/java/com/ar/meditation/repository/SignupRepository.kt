package com.ar.meditation.repository

import androidx.lifecycle.LiveData
import com.ar.meditation.DataBase.SignupDao
import com.ar.meditation.model.SignupModel

class SignupRepository(private val signupDao: SignupDao) {
    fun insert(signupModel: SignupModel) {
        signupDao.insert(signupModel)
    }

     fun getSignupByCnic(cnic: String): LiveData<List<SignupModel>> {
        return signupDao.getSignupByCnic(cnic)
    }
    fun getSignupByCnicAndPass(cnic: String, pass: String) : LiveData<List<SignupModel>> {
        return signupDao.getSignupByCnicAndPass(cnic, pass)
    }
}