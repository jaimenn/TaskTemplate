package com.example.tasks.service.listener

import com.example.tasks.service.repository.remote.model.ResponseLogin

interface APIListener<T> {
    fun onSuccess(t: T)
    fun onFailure(error: String)
}