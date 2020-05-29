package com.example.tasks.service.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity
class Task {
    @SerializedName("Id")
    var id: Int = 0
    @SerializedName("PriorityId")
    var priorityId: Int = 0
    @SerializedName("Description")
    var description: String = ""
    @SerializedName("DueDate")
    var dueDate: String = ""
    @SerializedName("Complete")
    var complete: Boolean = false
}