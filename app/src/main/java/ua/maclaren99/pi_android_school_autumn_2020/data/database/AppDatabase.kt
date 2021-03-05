package ua.maclaren99.pi_android_school_autumn_2020.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = arrayOf(Picture::class, User::class, Request::class, MyPhoto::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun pictureDAO(): PictureDAO
    abstract fun userDAO(): UserDAO
    abstract fun requestDAO(): RequestDAO
    abstract fun myPhotoDAO(): MyPhotoDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}