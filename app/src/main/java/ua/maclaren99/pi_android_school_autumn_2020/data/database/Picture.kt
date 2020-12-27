package ua.maclaren99.pi_android_school_autumn_2020.data.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "picture_table")
data class Picture(
    @NonNull
    val ownerLogin: String,
    @NonNull
    val url: String,
    val uri: String,
    val request: String = "empty",
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @NonNull
    val localID: Int? = null
)
