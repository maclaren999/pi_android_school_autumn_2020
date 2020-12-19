package ua.maclaren99.pi_android_school_autumn_2020.data.database

import androidx.room.*

@Dao
interface PictureDAO {

    @Insert
    fun insertUser(user: User)

    @Insert
    fun insertPicture(picture: Picture)

    @Insert
    fun insertRequest(request: Request)

    @Transaction
    @Query("SELECT * FROM picture_table WHERE ownerLogin = :login")
    fun testGetPicturesOfUser(login: String): List<Picture>

    @Transaction
    @Query("SELECT * FROM user_table WHERE login = :login")
    fun getUserPictures(login: String): UserPictures

    @Transaction
    @Query("SELECT * FROM user_table")
    fun getUsersAndPictures(): List<UserPictures>

    @Transaction
    @Query("SELECT * FROM request_table WHERE ownerLogin = :login ORDER BY time ASC")
    fun getUserHistory(login: String): List<Request>

    @Delete
    fun delete(picture: Picture)

    @Query("DELETE FROM picture_table")
    fun deleteAll()

    @Query("SELECT * from picture_table ORDER BY ownerLogin ASC")
    fun getAllWords(): List<Picture>?
}