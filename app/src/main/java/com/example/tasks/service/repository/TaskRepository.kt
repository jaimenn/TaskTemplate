package com.example.tasks.service.repository

import android.content.Context
import com.example.tasks.R
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.model.Task
import com.example.tasks.service.repository.remote.RetrofitClient
import com.example.tasks.service.repository.remote.TaskService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository(private val context: Context) : BaseRepository(context) {

    private val mRemote = RetrofitClient.createService(TaskService::class.java)

    fun all(listener: APIListener<List<Task>>) {
        list(mRemote.all(), listener)
    }

    fun nextWeek(listener: APIListener<List<Task>>) {
        list(mRemote.nextWeek(), listener)
    }

    fun overdue(listener: APIListener<List<Task>>) {
        list(mRemote.overdue(), listener)
    }

    private fun list(call: Call<List<Task>>, listener: APIListener<List<Task>>) {
        call.enqueue(object : Callback<List<Task>> {
            override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                if(response.code() == TaskConstants.HTTP.SUCCESS) {
                    response.body()?.let { listener.onSuccess(it) }
                } else {
                    val validation = Gson().fromJson(response.errorBody()!!.string(), String::class.java)
                    listener.onFailure(validation)
                }
            }


        })
    }

    fun create(task: Task, listener: APIListener<Boolean>) {
        // Verificação de internet
        if (!isConnectionAvailable(context)) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call: Call<Boolean> = mRemote.create(
            task.priorityId,
            task.description,
            task.dueDate,
            task.complete)

        call.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
               listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if(response.code() == TaskConstants.HTTP.SUCCESS) {
                    response.body()?.let { listener.onSuccess(it) }
                } else {
                    val validation = Gson().fromJson(response.errorBody()!!.string(), String::class.java)
                    listener.onFailure(validation)
                }
            }
        })
    }

    fun update(task: Task, listener: APIListener<Boolean>) {
        // Verificação de internet
        if (!isConnectionAvailable(context)) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call: Call<Boolean> = mRemote.update(
            task.id,
            task.priorityId,
            task.description,
            task.dueDate,
            task.complete)

        call.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if(response.code() == TaskConstants.HTTP.SUCCESS) {
                    response.body()?.let { listener.onSuccess(it) }
                } else {
                    val validation = Gson().fromJson(response.errorBody()!!.string(), String::class.java)
                    listener.onFailure(validation)
                }
            }
        })
    }

    fun get(id: Int, listener: APIListener<Task>) {

        // Verificação de internet
        if (!isConnectionAvailable(context)) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }


        val call: Call<Task> = mRemote.get(id)

        call.enqueue(object : Callback<Task> {
            override fun onFailure(call: Call<Task>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                if(response.code() == TaskConstants.HTTP.SUCCESS) {
                    response.body()?.let { listener.onSuccess(it) }
                } else {
                    val validation = Gson().fromJson(response.errorBody()!!.string(), String::class.java)
                    listener.onFailure(validation)
                }
            }
        })
    }

    fun delete(id: Int, listener: APIListener<Boolean>) {

        // Verificação de internet
        if (!isConnectionAvailable(context)) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call: Call<Boolean> = mRemote.delete(id)

        call.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if(response.code() == TaskConstants.HTTP.SUCCESS) {
                    response.body()?.let { listener.onSuccess(it) }
                } else {
                    val validation = Gson().fromJson(response.errorBody()!!.string(), String::class.java)
                    listener.onFailure(validation)
                }
            }
        })
    }

    fun updateStatus(id: Int, complete: Boolean, listener: APIListener<Boolean>) {

        // Verificação de internet
        if (!isConnectionAvailable(context)) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call : Call<Boolean>
        if(complete) {
            call = mRemote.complete(id)

        } else {
            call = mRemote.undo(id)
        }
        call.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                listener.onSuccess(true)
            }

        })
    }


}