package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.repository.local.SecurityPreferences

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val mSecurityPreferences = SecurityPreferences(application)

    private val mUserName = MutableLiveData<String>()
    var username: LiveData<String> = mUserName

    private val mLogOut = MutableLiveData<Boolean>()
    var logout: LiveData<Boolean> = mLogOut

    fun loadUserName() {
        val username = mSecurityPreferences.get(TaskConstants.SHARED.PERSON_NAME)

        mUserName.value = username
    }

    fun logout() {
        mSecurityPreferences.remove(TaskConstants.SHARED.PERSON_NAME)
        mSecurityPreferences.remove(TaskConstants.SHARED.TOKEN_KEY)
        mSecurityPreferences.remove(TaskConstants.SHARED.PERSON_KEY)

        mLogOut.value = true

    }
}