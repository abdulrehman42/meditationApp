package com.ar.meditation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ar.meditation.model.SignupModels
import com.ar.meditation.repository.SignupRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignupViewModel(private val repository: SignupRepository) : ViewModel() {
    fun insert(signupModel: SignupModels) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(signupModel)
    }

     fun getSignupByCnic(cnic: String): LiveData<List<SignupModels>> {
        return repository.getSignupByCnic(cnic)
    }
     fun getSignupByCnicAndPass(cnic: String, pass: String): LiveData<List<SignupModels>> {
        return repository.getSignupByCnicAndPass(cnic, pass)
    }
}