package com.example.tasks.service.repository.remote

import com.example.tasks.service.model.Priority
import retrofit2.Call
import retrofit2.http.GET

interface PriorityService {

    @GET("Priority")
    fun list() : Call<List<Priority>>

}