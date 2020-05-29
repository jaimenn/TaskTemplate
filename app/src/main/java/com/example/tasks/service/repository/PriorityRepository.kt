package com.example.tasks.service.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.model.Priority
import com.example.tasks.service.model.Task
import com.example.tasks.service.repository.local.TaskDatabase
import com.example.tasks.service.repository.remote.PriorityService
import com.example.tasks.service.repository.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepository(context: Context) {

    private val mRemote = RetrofitClient.createService(PriorityService::class.java)
    private val mPriorityDatabase = TaskDatabase.getDatabase(context).priorityDAO()

    fun all() {
        val call: Call<List<Priority>> = mRemote.list()
        call.enqueue(object : Callback<List<Priority>> {
            override fun onFailure(call: Call<List<Priority>>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call<List<Priority>>, response: Response<List<Priority>>) {
                if(response.code() == TaskConstants.HTTP.SUCCESS) {
                    (response.body())?.let {
                        mPriorityDatabase.clear()
                        mPriorityDatabase.save(it)
                    }
                }
            }

        })
    }

    fun list() : List<Priority> {
        return mPriorityDatabase.list()
    }

    fun getDescription(id: Int) = mPriorityDatabase.getDescription(id)

}