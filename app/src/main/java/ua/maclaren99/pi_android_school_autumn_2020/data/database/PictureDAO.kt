package ua.maclaren99.pi_android_school_autumn_2020.data.database

import androidx.room.*

@Dao
interface PictureDAO {

    @Insert
    fun insertPicture(picture: Picture)

    @Transaction
    @Query("SELECT * FROM picture_table WHERE ownerLogin = :login")
    fun testGetPicturesOfUser(login: String): List<Picture>
    //TODO("Change return type to Flow")

    @Delete
    fun delete(picture: Picture)

    @Query("DELETE FROM picture_table")
    fun deleteAll()

    @Query("SELECT * from picture_table ORDER BY ownerLogin ASC")
    fun getAllPictures(): List<Picture>?
}