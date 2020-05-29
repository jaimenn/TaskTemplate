package com.example.tasks.service.repository

import android.content.Context
import com.example.tasks.R
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.repository.remote.PersonService
import com.example.tasks.service.repository.remote.RetrofitClient
import com.example.tasks.service.repository.remote.model.ResponseLogin
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonRepository(val context: Context) : BaseRepository(context) {

    private val mRemote = RetrofitClient.createService(PersonService::class.java)

    fun create(name: String, email: String, password: String, listener: APIListener<ResponseLogin>) {

        // Verificação de internet
        if (!isConnectionAvailable(context)) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call: Call<ResponseLogin> = mRemote.create(email, password, name, true)
        call.enqueue(object : Callback<ResponseLogin> {
            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                if(response.code() == TaskConstants.HTTP.SUCCESS) {
                    response.body()?.let { listener.onSuccess(it) }
                } else {
                    val validation = Gson().fromJson(response.errorBody()!!.string(), String::class.java)
                    listener.onFailure(validation)
                }
            }

        })
    }

    fun login(email: String, password: String, listener: APIListener<ResponseLogin>) {
        // Verificação de internet
        if (!isConnectionAvailable(context)) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call: Call<ResponseLogin> = mRemote.login(email, password)
        call.enqueue(object : Callback<ResponseLogin> {
            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                if(response.code() == TaskConstants.HTTP.SUCCESS) {
                    response.body()?.let { listener.onSuccess(it) }
                } else {
                    val validation = Gson().fromJson(response.errorBody()!!.string(), String::class.java)
                    listener.onFailure(validation)
                }
            }

        })
    }
}