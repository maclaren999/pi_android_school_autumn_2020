package ua.maclaren99.pi_android_school_autumn_2020

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.*
import ua.maclaren99.pi_android_school_autumn_2020.data.network.AsyncFlickrSearchTask

class MainActivity : AppCompatActivity() {

    companion object {
        const val urlKey = "URL_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        search_button.setOnClickListener {
            asyncFlickrSearch()
        }
    }

    private fun asyncFlickrSearch() {
        val requestStr = search_edit_text.text.toString()
        if (!requestStr.isBlank())
            AsyncFlickrSearchTask(result_text, this).execute(requestStr)
    }

    override fun onBackPressed() {
        if (!web_view_layout.isVisible) {
            super.onBackPressed()
        } else {
            web_view_layout.visibility = View.GONE
            web_view.loadUrl("about:blank")
        }

    }

}