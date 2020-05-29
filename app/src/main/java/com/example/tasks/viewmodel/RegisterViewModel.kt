package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.repository.PersonRepository
import com.example.tasks.service.repository.local.SecurityPreferences
import com.example.tasks.service.repository.remote.RetrofitClient
import com.example.tasks.service.repository.remote.model.ResponseLogin

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val mSharedPreferences = SecurityPreferences(application)
    private val repository: PersonRepository = PersonRepository(application)

    private val mCreate = MutableLiveData<ValidationListener>()
    var create: LiveData<ValidationListener> = mCreate

    fun create(name: String, email: String, password: String) {
        repository.create(name, email, password, object : APIListener<ResponseLogin> {
            override fun onSuccess(responseLogin: ResponseLogin) {

                mSharedPreferences.store(TaskConstants.SHARED.PERSON_NAME, responseLogin.name)
                mSharedPreferences.store(TaskConstants.SHARED.PERSON_KEY, responseLogin.personKey)
                mSharedPreferences.store(TaskConstants.SHARED.TOKEN_KEY, responseLogin.token)

                RetrofitClient.addHeaders(responseLogin.token, responseLogin.personKey)

                mCreate.value = ValidationListener()
            }

            override fun onFailure(error: String) {
                mCreate.value = ValidationListener(error)
            }

        })
    }

}