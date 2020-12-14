package ua.maclaren99.pi_android_school_autumn_2020.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_favorites.*
import ua.maclaren99.pi_android_school_autumn_2020.R
import ua.maclaren99.pi_android_school_autumn_2020.data.database.AppDatabase
import ua.maclaren99.pi_android_school_autumn_2020.data.database.Picture
import ua.maclaren99.pi_android_school_autumn_2020.util.currentUser

class FavoritesActivity : AppCompatActivity() {

    companion object{
        lateinit var database: AppDatabase
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        initFields()
        testLog()
    }

    private fun initFields() {
        database = AppDatabase.getDatabase(this)
    }

    private fun testLog() {
        val favorites: List<Picture> = database.pictureDAO().getUserPictures(currentUser.login).pictures
        Glide.with(this)
            .load(favorites[0].uri)
            .into(test_image)

    }



}