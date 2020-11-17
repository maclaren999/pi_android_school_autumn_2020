package ua.maclaren99.pi_android_school_autumn_2020

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ua.maclaren99.pi_android_school_autumn_2020.data.initRetrofit
import ua.maclaren99.pi_android_school_autumn_2020.data.network.AsyncFlickrSearchTask
import ua.maclaren99.pi_android_school_autumn_2020.data.network.FlickrApiEndPoint

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRetrofit()
        search_button.setOnClickListener {
            val requestStr = search_edit_text.text.toString()

            AsyncFlickrSearchTask(result_text).execute(requestStr)

//                test(requestStr)
        }

    }



}