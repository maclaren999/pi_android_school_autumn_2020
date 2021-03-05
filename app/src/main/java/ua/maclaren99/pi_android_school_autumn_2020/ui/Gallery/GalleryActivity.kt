package ua.maclaren99.pi_android_school_autumn_2020.ui.Gallery

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_gallery.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.maclaren99.pi_android_school_autumn_2020.R
import ua.maclaren99.pi_android_school_autumn_2020.data.database.MyPhoto
import ua.maclaren99.pi_android_school_autumn_2020.ui.MainActivity.MainActivity
import ua.maclaren99.pi_android_school_autumn_2020.ui.MainActivity.MainActivity.Companion.KEY_TAKE_PHOTO
import ua.maclaren99.pi_android_school_autumn_2020.ui.MainActivity.PhotoUrlListAdapter
import ua.maclaren99.pi_android_school_autumn_2020.util.appDatabase
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class GalleryActivity : AppCompatActivity() {

    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: PhotoUrlListAdapter

    val REQUEST_IMAGE_CAPTURE = 2003

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        initRecyclerView()
        initButtons()

        if (intent.getBooleanExtra(KEY_TAKE_PHOTO, false)) dispatchTakePictureIntent()
    }

    private fun initButtons() {
        take_photo_button_gallery.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    private fun initRecyclerView() {
        mRecyclerView = gallery_list_recycler_view
        mAdapter = PhotoUrlListAdapter()
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter.onItemDeleteLambda = {pos, holder ->
            GlobalScope.launch(Dispatchers.IO) {
                appDatabase.myPhotoDAO().delete(MyPhoto(holder.urlTextView.text.toString()))
            }
        }

        GlobalScope.launch(Dispatchers.IO) {
            val photosURI = appDatabase.myPhotoDAO().getUserPhotos().map { it.uri }
            withContext(Dispatchers.Main) {
                mAdapter.addItems(*photosURI.toTypedArray())

                initItemTouchHelper()
            }
        }
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

    // TODO: 03.01.2021 Separate
    lateinit var savedPhotoUri: Uri
    lateinit var cacheFile: File
    private val authority = "ua.maclaren99.pi_android_school_autumn_2020.fileprovider"

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(isCache: Boolean = false): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = if (isCache) {
            cacheDir
        } else {
            getExternalFilesDir(Environment.DIRECTORY_PICTURES) // filesDir
        }
        return File(
            storageDir,
            "JPEG_${timeStamp}_.jpg" /* suffix */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            this.createNewFile()
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
                        authority,
                        it
                    )
                    savedPhotoUri = photoURI
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    cacheFile = it
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val cropURI = Uri.fromFile(createImageFile())

                    UCrop.of(savedPhotoUri, cropURI)
                        .withMaxResultSize(600, 600)
                        .start(this)
                    savedPhotoUri = cropURI
                } else {
                    cacheFile.delete()
                }
            }
            UCrop.REQUEST_CROP -> {
                if (resultCode == Activity.RESULT_OK) {
                    GlobalScope.launch(Dispatchers.IO) {
                        appDatabase.myPhotoDAO().insertPhoto(MyPhoto(savedPhotoUri.toString()))
                        withContext(Dispatchers.Main) {
                            mAdapter.addItems(savedPhotoUri.toString())
                        }
                    }
                    cacheFile.delete()
                    Toast.makeText(this, "Croped successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Crop failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}