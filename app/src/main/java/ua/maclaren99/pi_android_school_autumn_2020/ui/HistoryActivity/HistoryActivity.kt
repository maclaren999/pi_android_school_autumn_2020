package ua.maclaren99.pi_android_school_autumn_2020.ui.HistoryActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

import ua.maclaren99.pi_android_school_autumn_2020.R
import ua.maclaren99.pi_android_school_autumn_2020.data.database.Request
import ua.maclaren99.pi_android_school_autumn_2020.util.appDatabase
import ua.maclaren99.pi_android_school_autumn_2020.util.currentUser

class HistoryActivity : AppCompatActivity() {

    companion object {
        private lateinit var mRecyclerView: RecyclerView
        private lateinit var mAdapter: HistoryListAdapter
        private lateinit var mLayoutManager: LinearLayoutManager
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        initHistory()
    }

    private fun initHistory() {
        GlobalScope.launch(Dispatchers.Main) {
            val history: List<Request> = async(Dispatchers.IO) {
                appDatabase.requestDAO().getUserHistory(currentUser.login)
            }.await()

            Log.d(ua.maclaren99.pi_android_school_autumn_2020.util.TAG, history.toString())
            initRecyclerView(history)
        }
    }

    private fun initRecyclerView(history: List<Request>) {
        mRecyclerView = history_recycler_view
        mAdapter = HistoryListAdapter()
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter.addItems(
            *history.toTypedArray()
        )
    }
}