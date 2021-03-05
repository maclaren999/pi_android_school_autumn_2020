package ua.maclaren99.pi_android_school_autumn_2020.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface UserDAO {
    @Insert
    fun insertUser(user: User)

    @Transaction
    @Query("SELECT * FROM user_table WHERE login = :login")
    fun getUserPictures(login: String): UserPictures

    @Transaction
    @Query("SELECT * FROM user_table")
    fun getUsersAndPictures(): List<UserPictures>
}