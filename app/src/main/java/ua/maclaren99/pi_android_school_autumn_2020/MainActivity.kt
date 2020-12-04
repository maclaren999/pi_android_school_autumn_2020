package ua.maclaren99.pi_android_school_autumn_2020

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import ua.maclaren99.pi_android_school_autumn_2020.data.network.asyncFlickrSearchJob
import ua.maclaren99.pi_android_school_autumn_2020.util.hideKeyboard
import java.lang.ref.WeakReference


class MainActivity : AppCompatActivity() {

    companion object{
        lateinit var mRecyclerView: RecyclerView
        lateinit var mAdapter: PhotoUrlListAdapter
        lateinit var mLayoutManager: LinearLayoutManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()

        search_button.setOnClickListener {

            hideKeyboard()
            val requestStr = search_edit_text.text.toString()
            if (requestStr.isNotBlank()) {
                val meRecyclerView: WeakReference<RecyclerView> = WeakReference(photos_list_recycler_view)
                asyncFlickrSearchJob(requestStr, meRecyclerView)
            }
        }
    }

    private fun initRecyclerView() {
        mRecyclerView = photos_list_recycler_view
        mRecyclerView.adapter = PhotoUrlListAdapter()
        mRecyclerView.layoutManager = LinearLayoutManager(this)
    }


}

