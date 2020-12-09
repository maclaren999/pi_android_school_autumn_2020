package ua.maclaren99.pi_android_school_autumn_2020.data.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "picture_table")
data class Picture(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @NonNull
    val localID: Int,
    val title: String,
    @NonNull
    val url: String,
    val uri: String
)
