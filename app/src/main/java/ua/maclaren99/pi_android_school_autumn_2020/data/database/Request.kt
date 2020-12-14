package ua.maclaren99.pi_android_school_autumn_2020.data.database

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "request_table")
data class Request(
    @NonNull
    val request: String,
    @NonNull
    val ownerLogin: String,
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int? = null
    )