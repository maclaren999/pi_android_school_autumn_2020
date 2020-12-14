package ua.maclaren99.pi_android_school_autumn_2020.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ua.maclaren99.pi_android_school_autumn_2020.R
import ua.maclaren99.pi_android_school_autumn_2020.data.database.AppDatabase
import ua.maclaren99.pi_android_school_autumn_2020.data.database.Picture
import ua.maclaren99.pi_android_school_autumn_2020.data.database.saveImage
import ua.maclaren99.pi_android_school_autumn_2020.data.network.FlickrApiEndPoint.Companion.urlKey
import ua.maclaren99.pi_android_school_autumn_2020.util.currentUser


class WebViewActivity : AppCompatActivity() {

    companion object {
        lateinit var database: AppDatabase
        lateinit var imgUrl: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        title = getString(R.string.web_view_activity_title)

        imgUrl = intent.getStringExtra(urlKey) ?: return
        web_view_2.settings.loadWithOverviewMode = true
        web_view_2.settings.useWideViewPort = true
        web_view_2.settings.setSupportZoom(true)
        web_view_2.loadUrl(imgUrl)

        database = AppDatabase.getDatabase(this)

        initButtons()






    }

    private fun initButtons() {
        add_favorite_button.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                addPictureToFavorite(imgUrl)
                Log.d(
                    ua.maclaren99.pi_android_school_autumn_2020.util.TAG,
                    "onCreate: addPictureToFavorite()"
                )
            }
        }

        history_button.setOnClickListener {
            startActivity(
                Intent(baseContext, FavoritesActivity::class.java)
            )
        }
    }

    private suspend fun addPictureToFavorite(imgUrl: String) {


        var bitmap: Bitmap? = null

        Glide.with(this)
            .asBitmap()
            .load(imgUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    bitmap = resource
                    GlobalScope.launch(Dispatchers.IO) {
                        sendBitmapToRoom(resource, imgUrl)
                    }
                }
            })

//        TODO("Добавить кнопку ИЗБРАННОЕ и обработку")

//        TODO("Добавить активити со списком избранных и историей")
    }

    private suspend fun sendBitmapToRoom(resource: Bitmap, imgUrl: String) {
        val uri: Uri = saveImage(resource, this@WebViewActivity) ?: return

        Log.d(
            ua.maclaren99.pi_android_school_autumn_2020.util.TAG,
            "onResourceReady: sending to Room"
        )
        database.pictureDAO().insert(Picture(currentUser.login, imgUrl, uri.toString()))
    }
}