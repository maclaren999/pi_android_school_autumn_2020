package ua.maclaren99.pi_android_school_autumn_2020.ui.MainActivity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_main.*
import ua.maclaren99.pi_android_school_autumn_2020.R
import ua.maclaren99.pi_android_school_autumn_2020.data.network.asyncFlickrSearchJob
import ua.maclaren99.pi_android_school_autumn_2020.ui.FavoritesActivity.FavoritesActivity
import ua.maclaren99.pi_android_school_autumn_2020.ui.HistoryActivity.HistoryActivity
import ua.maclaren99.pi_android_school_autumn_2020.ui.MapsActivity.MapsActivity
import ua.maclaren99.pi_android_school_autumn_2020.util.hideKeyboard
import ua.maclaren99.pi_android_school_autumn_2020.util.runWithPermission
import java.io.File
import java.io.IOException
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    val REQUEST_IMAGE_CAPTURE = 2003
    private val REQUEST_LOCATION_PERMISSION: Int = 2001
    private val REQUEST_WRITE_EXTERNAL_PERMISSION: Int = 2002
    private val REQUEST_CAMERA_PERMISSION: Int = 2002
    private val TAG = ua.maclaren99.pi_android_school_autumn_2020.util.TAG

    companion object {
        const val KEY_savedInput = "SAVED_INPUT_MAIN_ACTIVITY"
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
                dispatchTakePictureIntent()
            }
        }
        //My photos gallery
        my_gallery_button.setOnClickListener {

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

    // TODO: 03.01.2021 Separate
    lateinit var currentPhotoPath: String
    lateinit var savedPhotoUri: Uri


    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES) // filesDir
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Toast.makeText(this, getString(R.string.cant_find_camera), Toast.LENGTH_LONG)
                        .show()
                    null
                }

                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "ua.maclaren99.pi_android_school_autumn_2020.fileprovider",
                        it
                    )
                    savedPhotoUri = photoURI
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
//                if (resultCode == Activity.RESULT_OK){
//                    UCrop.of(savedPhotoUri, destinationUri)
//                        .withAspectRatio(16, 9)
//                        .withMaxResultSize(maxWidth, maxHeight)
//                        .start(context);
            }
        }
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

