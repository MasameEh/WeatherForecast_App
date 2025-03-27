package com.example.weatherforecast_app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID


@Entity(tableName = "alert")
data class AlertInfo(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val timestamp: Long,
)
