package ua.maclaren99.pi_android_school_autumn_2020.ui.MainActivity

import android.content.Intent
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
import ua.maclaren99.pi_android_school_autumn_2020.util.hideKeyboard
import java.lang.ref.WeakReference


/*
* Удаление свайпом из списка
* Удаление избранного по кнопке
* Групировка избранных по запросам
* 
* */

class MainActivity : AppCompatActivity() {

    companion object {
        const val KEY_savedInput = "SAVED_INPUT_MAIN_ACTIVITY"
        private lateinit var mRecyclerView: RecyclerView
        lateinit var mAdapter: PhotoUrlListAdapter
        private lateinit var mLayoutManager: LinearLayoutManager
        lateinit var requestStr: String
    }

    //TODO("Requesting write storage permission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


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


}
