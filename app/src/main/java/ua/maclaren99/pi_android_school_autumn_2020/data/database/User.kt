package ua.maclaren99.pi_android_school_autumn_2020.data.database

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey
    @NonNull
    val login: String
)
