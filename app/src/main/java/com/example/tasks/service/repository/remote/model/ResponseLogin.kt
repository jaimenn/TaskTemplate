package com.example.tasks.service.repository.remote.model

import com.google.gson.annotations.SerializedName

class ResponseLogin {
    @SerializedName("token")
    var token: String = ""
    @SerializedName("personKey")
    var personKey: String = ""
    @SerializedName("name")
    var name: String = ""

}