package ua.maclaren99.pi_android_school_autumn_2020.ui.FavoritesActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_favorites.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ua.maclaren99.pi_android_school_autumn_2020.R
import ua.maclaren99.pi_android_school_autumn_2020.data.database.AppDatabase
import ua.maclaren99.pi_android_school_autumn_2020.data.database.Picture
import ua.maclaren99.pi_android_school_autumn_2020.util.appDatabase
import ua.maclaren99.pi_android_school_autumn_2020.util.currentUser

class FavoritesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

//        initFields()
        testLog()
    }

//    private fun initFields() {
//    }

    private fun testLog() {
        GlobalScope.launch(Dispatchers.Main) {
//
//            TODO("Recycler view with swipe delete")
            val favorites: List<Picture> = async(Dispatchers.IO) {
                appDatabase.pictureDAO().getUserPictures(currentUser.login).pictures
            }.await()

            Log.d(ua.maclaren99.pi_android_school_autumn_2020.util.TAG, favorites.toString())
            Glide.with(this@FavoritesActivity)
                .load(favorites[0].uri)
                .into(test_image)
        }

    }


}