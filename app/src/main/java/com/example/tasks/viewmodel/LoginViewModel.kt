package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.repository.PersonRepository
import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.service.repository.local.SecurityPreferences
import com.example.tasks.service.repository.remote.RetrofitClient
import com.example.tasks.service.repository.remote.model.ResponseLogin

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val mSecurityPreferences = SecurityPreferences(application)
    private val repository: PersonRepository = PersonRepository(application)
    private val mPriorityRepository : PriorityRepository = PriorityRepository(application)
    private val mLogin = MutableLiveData<ValidationListener>()
    var login: LiveData<ValidationListener> = mLogin

    private val mLoggedUser = MutableLiveData<Boolean>()
    var loggedUser: LiveData<Boolean> = mLoggedUser


    /**
     * Faz login usando API
     */
    fun doLogin(email: String, password: String) {
        repository.login(email, password, object : APIListener<ResponseLogin> {
            override fun onSuccess(responseLogin: ResponseLogin) {

                mSecurityPreferences.store(TaskConstants.SHARED.PERSON_NAME, responseLogin.name)
                mSecurityPreferences.store(TaskConstants.SHARED.PERSON_KEY, responseLogin.personKey)
                mSecurityPreferences.store(TaskConstants.SHARED.TOKEN_KEY, responseLogin.token)

                RetrofitClient.addHeaders(responseLogin.token, responseLogin.personKey)

                mLogin.value = ValidationListener()
            }

            override fun onFailure(error: String) {
                mLogin.value = ValidationListener(error)
            }

        })
    }

    /**
     * Verifica se usuário está logado
     */
    fun verifyLoggedUser() {
        val personKey = mSecurityPreferences.get(TaskConstants.SHARED.PERSON_KEY)
        val tokenKey = mSecurityPreferences.get(TaskConstants.SHARED.TOKEN_KEY)

        // Se token e person key forem diferentes de vazio, usuário está logado
        val logged = (tokenKey != "" && personKey != "")

        // Atualiza valores de Header para requisições
        RetrofitClient.addHeaders(personKey, tokenKey)

        mLoggedUser.value = logged

        if(!logged) {
            mPriorityRepository.all()
        }

    }

}