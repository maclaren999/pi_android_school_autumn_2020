package ua.maclaren99.pi_android_school_autumn_2020.ui.MainActivity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.day_night_item_layout.*
import kotlinx.android.synthetic.main.day_night_item_layout.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ua.maclaren99.pi_android_school_autumn_2020.R
import ua.maclaren99.pi_android_school_autumn_2020.data.database.MyPhoto
import ua.maclaren99.pi_android_school_autumn_2020.data.network.asyncFlickrSearchJob
import ua.maclaren99.pi_android_school_autumn_2020.ui.FavoritesActivity.FavoritesActivity
import ua.maclaren99.pi_android_school_autumn_2020.ui.Gallery.GalleryActivity
import ua.maclaren99.pi_android_school_autumn_2020.ui.HistoryActivity.HistoryActivity
import ua.maclaren99.pi_android_school_autumn_2020.ui.MapsActivity.MapsActivity
import ua.maclaren99.pi_android_school_autumn_2020.util.appDatabase
import ua.maclaren99.pi_android_school_autumn_2020.util.hideKeyboard
import ua.maclaren99.pi_android_school_autumn_2020.util.runWithPermission
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private val REQUEST_LOCATION_PERMISSION: Int = 2001
    private val REQUEST_WRITE_EXTERNAL_PERMISSION: Int = 2002
    private val REQUEST_CAMERA_PERMISSION: Int = 2002
    private val TAG = ua.maclaren99.pi_android_school_autumn_2020.util.TAG

    companion object {

        const val KEY_savedInput = "SAVED_INPUT_MAIN_ACTIVITY"
        const val KEY_TAKE_PHOTO = "TAKE_PHOTO"
        private lateinit var mRecyclerView: RecyclerView
        lateinit var mAdapter: PhotoUrlListAdapter
        private lateinit var mLayoutManager: LinearLayoutManager
        var requestStr: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFields()
        initRecyclerView()
        initButtons()

        Toast.makeText(this, "works", Toast.LENGTH_LONG).show()

    }

    private fun initButtons() {
        search_button.setOnClickListener {
            hideKeyboard()
            requestStr = search_edit_text.text.toString()
            if (requestStr.isNotBlank()) {
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
        //MapsActivity
        location_button.setOnClickListener {
            startMapsActivity()
        }
        //Take photo
        take_photo_button.setOnClickListener {
            runWithPermission(Manifest.permission.CAMERA, REQUEST_CAMERA_PERMISSION) {
                startActivity(Intent(this, GalleryActivity::class.java)
                    .putExtra(KEY_TAKE_PHOTO, true))

            }
        }
        //My photos gallery
        my_gallery_button.setOnClickListener {
            runWithPermission(Manifest.permission.CAMERA, REQUEST_CAMERA_PERMISSION) {
                startActivity(Intent(this, GalleryActivity::class.java))
            }
        }
    }

    private fun initFields() {
        requestDataPermission()
    }

    private fun requestDataPermission() {
        runWithPermission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            REQUEST_WRITE_EXTERNAL_PERMISSION
        ) {}
    }

    private fun startMapsActivity() {
        runWithPermission(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_LOCATION_PERMISSION) {
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu, menu)

        val switchThemeItem = menu?.findItem(R.id.switch_theme_item)
        switchThemeItem?.setActionView(R.layout.day_night_item_layout)
        switchThemeItem?.actionView?.setOnClickListener {
            Toast.makeText(this, "day_night layout clicked", Toast.LENGTH_SHORT).show()
            it.switch_to_night.visibility = View.GONE
            it.switch_to_day.visibility = View.VISIBLE
        }
        return true
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION ->             // If the permission is granted, get the location,
                // otherwise, show a Toast
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    startMapsActivity()
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

