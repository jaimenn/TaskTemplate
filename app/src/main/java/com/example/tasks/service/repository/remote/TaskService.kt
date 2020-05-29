package com.example.tasks.service.repository.remote

import com.example.tasks.service.model.Task
import com.example.tasks.service.repository.remote.model.ResponseLogin
import retrofit2.Call
import retrofit2.http.*

interface TaskService {

    @GET("Task")
    fun all() : Call<List<Task>>

    @GET("Task/{id}")
    fun get(@Path(value = "id") id: Int) : Call<Task>

    @GET("Task/Next7Days")
    fun nextWeek() : Call<List<Task>>

    @GET("Task/Overdue")
    fun overdue() : Call<List<Task>>

    @GET("Task/{id}")
    fun overdue(@Path(value = "id", encoded = true) id: Int) : Call<Task>

    @FormUrlEncoded
    @POST("Task")
    fun create(@Field("PriorityId") priorityId: Int,
               @Field("Description") description: String,
               @Field("DueDate") dueDate: String,
               @Field("Complete") complete: Boolean) : Call<Boolean>


    @FormUrlEncoded
    @HTTP(method = "PUT", path = "Task", hasBody = true)
    fun update(@Field("Id") id: Int,
               @Field("PriorityId") priorityId: Int,
               @Field("Description") description: String,
               @Field("DueDate") dueDate: String,
               @Field("Complete") complete: Boolean) : Call<Boolean>

    @FormUrlEncoded
    @HTTP(method = "PUT", path = "Task/Complete", hasBody = true)
    fun complete(@Field("Id") id: Int) : Call<Boolean>

    @FormUrlEncoded
    @HTTP(method = "PUT", path = "Task/Undo", hasBody = true)
    fun undo(@Field("Id") id: Int) : Call<Boolean>

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "Task", hasBody = true)
    fun delete(@Field("Id") id: Int) : Call<Boolean>

}