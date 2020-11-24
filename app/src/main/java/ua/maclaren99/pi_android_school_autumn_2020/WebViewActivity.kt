package ua.maclaren99.pi_android_school_autumn_2020

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_web_view.*
import ua.maclaren99.pi_android_school_autumn_2020.data.network.AsyncFlickrSearchTask.Companion.urlKey

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        title = getString(R.string.web_view_activity_title)

        val imgUrl = intent.getStringExtra(urlKey)
        web_view_2.settings.loadWithOverviewMode = true
        web_view_2.settings.useWideViewPort = true
        web_view_2.settings.setSupportZoom(true)
        web_view_2.loadUrl(imgUrl)
    }
}