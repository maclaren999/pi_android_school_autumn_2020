package ua.maclaren99.pi_android_school_autumn_2020.data.database

import androidx.room.Embedded
import androidx.room.Relation

data class UserPictures(
    @Embedded val user: User,
    @Relation(
        parentColumn = "login",
        entityColumn = "ownerLogin"
    )
    val pictures: List<Picture>
)
