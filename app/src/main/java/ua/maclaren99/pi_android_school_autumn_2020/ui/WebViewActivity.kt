package ua.maclaren99.pi_android_school_autumn_2020.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ua.maclaren99.pi_android_school_autumn_2020.R
import ua.maclaren99.pi_android_school_autumn_2020.data.database.Picture
import ua.maclaren99.pi_android_school_autumn_2020.data.database.saveImage
import ua.maclaren99.pi_android_school_autumn_2020.data.network.FlickrApiEndPoint.Companion.KEY_LOCAL_RESOURCE_MODE
import ua.maclaren99.pi_android_school_autumn_2020.data.network.FlickrApiEndPoint.Companion.KEY_REQUEST_TEXT
import ua.maclaren99.pi_android_school_autumn_2020.data.network.FlickrApiEndPoint.Companion.KEY_URL
import ua.maclaren99.pi_android_school_autumn_2020.ui.FavoritesActivity.FavoritesActivity
import ua.maclaren99.pi_android_school_autumn_2020.ui.HistoryActivity.HistoryActivity
import ua.maclaren99.pi_android_school_autumn_2020.util.appDatabase
import ua.maclaren99.pi_android_school_autumn_2020.util.currentUser


class WebViewActivity : AppCompatActivity() {

    companion object {
        lateinit var imgUrl: String
        lateinit var requestStr: String
        var isLocalMode: Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        title = getString(R.string.web_view_activity_title)

        initContent()
        initButtons()

    }

    private fun initContent() {
        isLocalMode = intent.getBooleanExtra(KEY_LOCAL_RESOURCE_MODE, false)
        imgUrl = intent.getStringExtra(KEY_URL) ?: return
        requestStr = intent.getStringExtra(KEY_REQUEST_TEXT) ?: ""
        request_string_view.text = requestStr
        web_view_2.settings.loadWithOverviewMode = true
        web_view_2.settings.useWideViewPort = true
        web_view_2.settings.setSupportZoom(true)
        web_view_2.loadUrl(imgUrl)
    }

    private fun initButtons() {
        if (!isLocalMode) {
            add_favorite_button.setOnClickListener {
                GlobalScope.launch(Dispatchers.Main) {
                    addPictureToFavorite(imgUrl)
                    Log.d(
                        ua.maclaren99.pi_android_school_autumn_2020.util.TAG,
                        "onCreate: addPictureToFavorite()"
                    )
                    add_favorite_button.visibility = View.GONE
                    add_favorite_filled_button.visibility = View.VISIBLE
                }
            }
            add_favorite_filled_button.setOnClickListener {
                Toast.makeText(this, "TODO: Implement deleting img", Toast.LENGTH_SHORT).show()
                //TODO("Deleting img")
            }
        } else {
            add_favorite_button.visibility = View.INVISIBLE
            add_favorite_filled_button.visibility = View.INVISIBLE
        }

        history_button.setOnClickListener {
            startActivity(
                Intent(baseContext, HistoryActivity::class.java)
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
                        try {
                            sendBitmapToRoom(resource, imgUrl)
                        } catch (e: Error) {
                            Log.e(ua.maclaren99.pi_android_school_autumn_2020.util.TAG, e.message)
                        }
                    }
                }
            })
    }

    private suspend fun sendBitmapToRoom(resource: Bitmap, imgUrl: String) {
        val uri: Uri = saveImage(resource, this@WebViewActivity) ?: return

        Log.d(
            ua.maclaren99.pi_android_school_autumn_2020.util.TAG,
            "onResourceReady: sending to Room"
        )
        appDatabase.pictureDAO()
            .insertPicture(Picture(currentUser.login, imgUrl, uri.toString(), requestStr))
    }
}