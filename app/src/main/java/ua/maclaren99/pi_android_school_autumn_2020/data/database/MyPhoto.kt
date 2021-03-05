package ua.maclaren99.pi_android_school_autumn_2020.data.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ua.maclaren99.pi_android_school_autumn_2020.util.currentUser

@Entity(tableName = "my_photo_table")
data class MyPhoto(
    @NonNull
    val uri: String,
    @NonNull
    val ownerLogin: String = currentUser.login,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @NonNull
    val localID: Int? = null
)