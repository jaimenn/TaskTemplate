package com.example.tasks.service.repository.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tasks.service.model.Priority

@Dao
interface PriorityDAO {

    @Insert
    fun save(priorities: List<Priority>)

    @Query("DELETE FROM priority")
    fun clear()

    @Query("SELECT * FROM priority")
    fun list(): List<Priority>

    @Query("SELECT description FROM priority WHERE id = :id")
    fun getDescription(id: Int): String
}