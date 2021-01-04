package ua.maclaren99.pi_android_school_autumn_2020.ui.MapsActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_flickr_location.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ua.maclaren99.pi_android_school_autumn_2020.R
import ua.maclaren99.pi_android_school_autumn_2020.data.network.FlickrApiEndPoint
import ua.maclaren99.pi_android_school_autumn_2020.ui.MainActivity.PhotoUrlListAdapter

class FlickrLocationActivity : AppCompatActivity() {


    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: PhotoUrlListAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mLatLng: LatLng
    private val defaultKyivLatLng: Pair<Double, Double> = Pair(50.4547, 30.5238)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flickr_location)

        mLatLng = LatLng(
            intent.extras?.getDouble(MapsActivity.KEY_LATITUDE) ?: defaultKyivLatLng.first,
            intent.extras?.getDouble(MapsActivity.KEY_LONGITUDE) ?: defaultKyivLatLng.second
        )

        initRecyclerView()
        flickrSearch()
    }

    private fun flickrSearch() {
        val resultJob =
            GlobalScope.launch(Dispatchers.Main) {
                val resultList = async(Dispatchers.IO) {
                    FlickrApiEndPoint.doSearchLatLon(mLatLng.latitude, mLatLng.longitude, FlickrApiEndPoint())
                }.await()

                mAdapter.removeAll()
                mAdapter.addItems(*resultList.toTypedArray())
            }

    }

    private fun initRecyclerView() {
        mRecyclerView = recycler_locations
        mAdapter = PhotoUrlListAdapter()
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        initItemTouchHelper()
    }

    private fun initItemTouchHelper() {
        val helper = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {
                    val position = viewHolder.adapterPosition
                    // Delete the word
                    mAdapter.removeItems(position)
                }
            })

        helper.attachToRecyclerView(mRecyclerView)
    }

}