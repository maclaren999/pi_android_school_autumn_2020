package ua.maclaren99.pi_android_school_autumn_2020.ui.HistoryActivity

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_history_request.view.*
import ua.maclaren99.pi_android_school_autumn_2020.R
import ua.maclaren99.pi_android_school_autumn_2020.data.database.Request


class HistoryListAdapter : RecyclerView.Adapter<HistoryListAdapter.HistoryListViewHolder>() {

    private val historyList = mutableListOf<Request>()

    class HistoryListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val urlTextView = view.request_text_view
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryListViewHolder =
        HistoryListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_history_request, parent, false)
        )

    override fun onBindViewHolder(holder: HistoryListViewHolder, position: Int) {
        holder.urlTextView.text = historyList[position].request.toString()
    }

    override fun getItemCount(): Int = historyList.size

    fun addItems(vararg elements: Request) {
        val prevSize = historyList.size
        historyList.addAll(elements)
        notifyItemRangeInserted(prevSize + 1, elements.size)
    }

    fun removeItems(vararg elements: Request) {
        historyList.removeAll(elements)
    }

//    @RequiresApi(Build.VERSION_CODES.N)
    fun remove(request: String) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        historyList.removeIf {
            it.request == request
        }
    }
}
}