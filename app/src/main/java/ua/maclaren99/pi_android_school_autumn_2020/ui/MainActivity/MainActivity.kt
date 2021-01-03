package ua.maclaren99.pi_android_school_autumn_2020.ui.MainActivity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import ua.maclaren99.pi_android_school_autumn_2020.R
import ua.maclaren99.pi_android_school_autumn_2020.data.network.asyncFlickrSearchJob
import ua.maclaren99.pi_android_school_autumn_2020.ui.FavoritesActivity.FavoritesActivity
import ua.maclaren99.pi_android_school_autumn_2020.ui.HistoryActivity.HistoryActivity
import ua.maclaren99.pi_android_school_autumn_2020.ui.MapsActivity.MapsActivity
import ua.maclaren99.pi_android_school_autumn_2020.util.hideKeyboard
import ua.maclaren99.pi_android_school_autumn_2020.util.runWithPermission
import java.lang.ref.WeakReference



class MainActivity : AppCompatActivity() {

    private val REQUEST_LOCATION_PERMISSION: Int = 2001
    private val REQUEST_WRITE_EXTERNAL_PERMISSION: Int = 2002
    private val TAG= ua.maclaren99.pi_android_school_autumn_2020.util.TAG

    companion object {
        const val KEY_savedInput = "SAVED_INPUT_MAIN_ACTIVITY"
        private lateinit var mRecyclerView: RecyclerView
        lateinit var mAdapter: PhotoUrlListAdapter
        private lateinit var mLayoutManager: LinearLayoutManager
        var requestStr: String = ""
    }

    //TODO("Requesting write storage permission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFields()
        initRecyclerView()
        initButtons()


    }

    private fun initButtons() {
        search_button.setOnClickListener {
            hideKeyboard()
            requestStr = search_edit_text.text.toString()
            if (requestStr.isNotBlank()) {
                val meRecyclerView: WeakReference<RecyclerView> = WeakReference(
                    photos_list_recycler_view
                )
                asyncFlickrSearchJob(requestStr/*, meRecyclerView*/)
            }
        }
        //Favorite
        favorites_button.setOnClickListener {

            startActivity(Intent(baseContext, FavoritesActivity::class.java))
        }
        //History
        history_button.setOnClickListener {
            startActivity(Intent(baseContext, HistoryActivity::class.java))
        }

        location_button.setOnClickListener {
            checkLocationPermission()
        }
    }

    private fun initFields() {
        requestDataPermission()
    }

    private fun requestDataPermission() {
        runWithPermission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            REQUEST_WRITE_EXTERNAL_PERMISSION
        ){

        }
    }


    private fun checkLocationPermission(){
        runWithPermission(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_LOCATION_PERMISSION){
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
    }



    private fun initRecyclerView() {
        mRecyclerView = photos_list_recycler_view
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

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val input = search_edit_text.text.toString()
        savedInstanceState.putString(KEY_savedInput, input)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val input = outState.getString(KEY_savedInput)
        search_edit_text.setText(input)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION ->             // If the permission is granted, get the location,
                // otherwise, show a Toast
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    checkLocationPermission()
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.location_required),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            REQUEST_WRITE_EXTERNAL_PERMISSION ->
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {

                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.storage_permission_needed),
                        Toast.LENGTH_SHORT
                    ).show()
                }

        }
    }

}

