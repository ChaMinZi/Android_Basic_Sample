package com.udacity.shoestore.login

import android.text.Editable
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber

class LoginViewModel : ViewModel() {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _isLoginEnabled = MutableLiveData<Boolean>()
    val isLoginEnabled : LiveData<Boolean> get() = _isLoginEnabled

    fun onSignInAndUp() {
        _isLoginEnabled.value = (!email.value.isNullOrBlank()
                && !password.value.isNullOrBlank()
                && Patterns.EMAIL_ADDRESS.matcher(email.value).matches())
    }

    fun onLoginComplete() {
        _isLoginEnabled.value = false
    }

    override fun onCleared() {
        super.onCleared()
    }
}