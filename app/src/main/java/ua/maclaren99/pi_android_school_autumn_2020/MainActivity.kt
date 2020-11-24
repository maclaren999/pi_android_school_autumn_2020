package ua.maclaren99.pi_android_school_autumn_2020

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ua.maclaren99.pi_android_school_autumn_2020.data.network.asyncFlickrSearchJob
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        search_button.setOnClickListener {
            val requestStr = search_edit_text.text.toString()
            if (requestStr.isNotBlank()) {
                val meTextView: WeakReference<TextView> = WeakReference(result_text)
                asyncFlickrSearchJob(requestStr, meTextView)
            }
        }
    }


}

