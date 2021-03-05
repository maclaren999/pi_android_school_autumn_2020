package ua.maclaren99.pi_android_school_autumn_2020.data.database

import androidx.room.*
import ua.maclaren99.pi_android_school_autumn_2020.util.currentUser

@Dao
interface MyPhotoDAO {
    @Insert
    fun insertPhoto(photo: MyPhoto)

    @Transaction
    @Query("SELECT * FROM my_photo_table WHERE ownerLogin = :login")
    fun getUserPhotos(login: String = currentUser.login): List<MyPhoto>
    //TODO("Change return type to Flow")

    @Delete
    fun delete(photo: MyPhoto)

    @Query("DELETE FROM my_photo_table")
    fun deleteAll()

    @Query("SELECT * from my_photo_table ORDER BY ownerLogin ASC")
    fun getAllPictures(): List<MyPhoto>?
}