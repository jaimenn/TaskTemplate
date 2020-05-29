package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.model.Priority
import com.example.tasks.service.model.Task
import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.service.repository.TaskRepository

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {

    private val mTaskRepository = TaskRepository(application)
    private val mRepository = PriorityRepository(application)

    private val mPriorityList = MutableLiveData<List<Priority>>()
    val priorities: LiveData<List<Priority>> = mPriorityList

    private val mValidation = MutableLiveData<ValidationListener>()
    val validation: LiveData<ValidationListener> = mValidation

    private val mTask = MutableLiveData<Task>()
    val task: LiveData<Task> = mTask

    fun listPriorities() {
        mPriorityList.value = mRepository.list()
    }

    fun save(task: Task) {
        if(task.id == 0) {
            mTaskRepository.create(task, object : APIListener<Boolean> {
                override fun onSuccess(t: Boolean) {
                    mValidation.value = ValidationListener()
                }

                override fun onFailure(error: String) {
                    mValidation.value = ValidationListener(error)
                }

            })
        } else {
            mTaskRepository.update(task, object : APIListener<Boolean> {
                override fun onSuccess(t: Boolean) {
                    mValidation.value = ValidationListener()
                }

                override fun onFailure(error: String) {
                    mValidation.value = ValidationListener(error)
                }

            })
        }

    }

    fun get(id: Int) {
        mTaskRepository.get(id, object : APIListener<Task> {
            override fun onSuccess(t: Task) {
                mTask.value = t
            }

            override fun onFailure(error: String) {

            }

        })
    }
}