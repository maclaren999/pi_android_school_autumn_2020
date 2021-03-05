package ua.maclaren99.pi_android_school_autumn_2020.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface RequestDAO {
    @Transaction

    @Query("SELECT * FROM request_table WHERE ownerLogin = :login ORDER BY time ASC")
    fun getUserHistory(login: String): List<Request>

    @Insert
    fun insertRequest(request: Request)
}