package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.model.Task
import com.example.tasks.service.repository.TaskRepository

class AllTasksViewModel(application: Application) : AndroidViewModel(application) {

    private var mTaskFilter = 0

    private val mTaskRepository = TaskRepository(application)
    private val mList = MutableLiveData<List<Task>>()
    var task : LiveData<List<Task>> = mList

    private val mValidation = MutableLiveData<ValidationListener>()
    var validation : LiveData<ValidationListener> = mValidation

    fun list(taskFilter : Int) {
        mTaskFilter = taskFilter
        val listener = object : APIListener<List<Task>> {
            override fun onSuccess(t: List<Task>) {
                mList.value = t
            }

            override fun onFailure(error: String) {
                mList.value = arrayListOf()
            }

        }
        when(mTaskFilter) {
            TaskConstants.FILTER.ALL -> {
                mTaskRepository.all(listener)
            }
            TaskConstants.FILTER.EXPIRED -> {
                mTaskRepository.overdue(listener)
            }
            TaskConstants.FILTER.NEXT -> {
                mTaskRepository.nextWeek(listener)
            }
        }

    }

    fun delete(id: Int) {
        mTaskRepository.delete(id, object : APIListener<Boolean> {
            override fun onSuccess(t: Boolean) {
                list(mTaskFilter)
            }

            override fun onFailure(error: String) {}

        })
    }

    fun complete(id: Int) {
        mTaskRepository.updateStatus(id, true, object : APIListener<Boolean> {
            override fun onSuccess(t: Boolean) {
                list(mTaskFilter)
            }

            override fun onFailure(error: String) {}

        })
    }

    fun undo(id: Int) {
        mTaskRepository.updateStatus(id, false, object : APIListener<Boolean> {
            override fun onSuccess(t: Boolean) {
                list(mTaskFilter)
            }

            override fun onFailure(error: String) {}

        })
    }
}