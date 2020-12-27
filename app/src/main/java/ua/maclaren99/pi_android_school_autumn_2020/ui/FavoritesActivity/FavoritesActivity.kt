package ua.maclaren99.pi_android_school_autumn_2020.ui.FavoritesActivity

import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_favorites.*
import kotlinx.coroutines.*
import ua.maclaren99.pi_android_school_autumn_2020.R
import ua.maclaren99.pi_android_school_autumn_2020.data.database.Picture
import ua.maclaren99.pi_android_school_autumn_2020.util.appDatabase
import ua.maclaren99.pi_android_school_autumn_2020.util.currentUser

class FavoritesActivity : AppCompatActivity() {

    companion object {
        private lateinit var mRecyclerView: RecyclerView
        private lateinit var mAdapter: FavoritesListAdapter
        private lateinit var mLayoutManager: LinearLayoutManager
        private lateinit var favoritesList: List<Picture>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)


        initFields()
    }

    private fun initFields() {
        mRecyclerView = favorites_recycler
        mAdapter = FavoritesListAdapter()
        mLayoutManager = LinearLayoutManager(this)

        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = mLayoutManager
        initItemTouchHelper()

        GlobalScope.launch(Dispatchers.Main) {
            favoritesList = withContext(Dispatchers.IO) {
                appDatabase.pictureDAO().getUserPictures(currentUser.login).pictures
            }

            mAdapter.addItems(*favoritesList.toTypedArray())
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
                    GlobalScope.launch(Dispatchers.Main) {
                        deletePicture(position)
                    }
                }
            })

        helper.attachToRecyclerView(mRecyclerView)
    }

    suspend fun deletePicture(position: Int) {
        mAdapter.removeItems(position)
        withContext(Dispatchers.IO) {
            appDatabase.pictureDAO().delete(favoritesList[position])
        }
    }

}