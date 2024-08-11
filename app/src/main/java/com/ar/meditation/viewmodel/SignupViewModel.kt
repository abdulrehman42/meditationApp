package com.ar.meditation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ar.meditation.model.SignupModel
import com.ar.meditation.repository.SignupRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignupViewModel(private val repository: SignupRepository) : ViewModel() {
    fun insert(signupModel: SignupModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(signupModel)
    }

     fun getSignupByCnic(cnic: String): LiveData<List<SignupModel>> {
        return repository.getSignupByCnic(cnic)
    }
     fun getSignupByCnicAndPass(cnic: String, pass: String): LiveData<List<SignupModel>> {
        return repository.getSignupByCnicAndPass(cnic, pass)
    }
}