package com.ar.meditation.repository;

import androidx.lifecycle.LiveData;

import com.ar.meditation.DataBase.SignupDao;
import com.ar.meditation.model.SignupModels;

import java.util.List;

public class SignupRepository {
    private final SignupDao signupDao;

    public SignupRepository(SignupDao signupDao) {
        this.signupDao = signupDao;
    }

    public void insert(SignupModels signupModel) {
        // Perform the insert operation in a background thread or use an executor
        new Thread(() -> signupDao.insert(signupModel)).start();
    }

    public LiveData<List<SignupModels>> getSignupByCnic(String cnic) {
        return signupDao.getSignupByCnic(cnic);
    }

    public LiveData<List<SignupModels>> getSignupByCnicAndPass(String cnic, String pass) {
        return signupDao.getSignupByCnicAndPass(cnic, pass);
    }
}
