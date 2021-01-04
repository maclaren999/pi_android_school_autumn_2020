package ua.maclaren99.pi_android_school_autumn_2020.ui.FavoritesActivity

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.item_header.view.*
import kotlinx.android.synthetic.main.item_photo_card.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.maclaren99.pi_android_school_autumn_2020.R
import ua.maclaren99.pi_android_school_autumn_2020.data.database.Picture
import ua.maclaren99.pi_android_school_autumn_2020.util.appDatabase
import ua.maclaren99.pi_android_school_autumn_2020.util.displayWebViewActivity


class FavoritesListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

 private val TAG = ua.maclaren99.pi_android_school_autumn_2020.util.TAG

    companion object{
        const val ITEM_PICTURE = 0
        const val ITEM_HEADER = 1
    }

    private val data: MutableList<Any> = mutableListOf<Any>()

    class FavoritePhotoItemHolder(view: View) :
        RecyclerView.ViewHolder(view)/*, View.OnClickListener*/ {
        val urlTextView: TextView = view.url_text_view
        val photoPreview: ImageView = view.photo_preview
        val innerLayout = view.item_photo_card_clayout
        val closeButton = view.close_button
    }

    class FavoriteReqestItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        val request: TextView = view.request_header_text_view
    }

    override fun getItemViewType(position: Int): Int {
        val type = when (data[position]) {
            is Picture -> ITEM_PICTURE
            is String -> ITEM_HEADER
            else -> ITEM_PICTURE
        }
        Log.d(TAG, "getItemViewType: $type")
        return type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            ITEM_HEADER -> FavoriteReqestItemHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false)
            ).also { Log.d(TAG, "onCreateViewHolder: HEADER") }
            else -> FavoritePhotoItemHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_photo_card, parent, false)
            ).also { Log.d(TAG, "onCreateViewHolder: PICTURE") }
        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FavoritePhotoItemHolder -> {
                val picture = data[position] as Picture
                holder.urlTextView.text = picture.url

                holder.closeButton.setOnClickListener {
                    GlobalScope.launch(Dispatchers.Main) {
                        withContext(Dispatchers.IO) {
                            appDatabase.pictureDAO().delete(picture)
                        }
                        removeItems(position)
                    }
                }

                holder.itemView.setOnClickListener {
                    displayWebViewActivity(holder.urlTextView.context, (data[position] as Picture))
                }

                Glide
                    .with(holder.urlTextView.context)
                    .load(picture.uri)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .centerCrop()
                    .into(holder.photoPreview)
            }
            else -> {
                (holder as FavoriteReqestItemHolder).request.text = data[position] as String
            }

        }
    }

    override fun getItemCount(): Int = data.size

    fun setNewData(vararg elements: Picture) {
//        val prevSize = data.size
        Log.d(TAG, "setNewData: ${elements.toString()}")
        data.clear()
        data.addAll(addHeaders(elements))
        notifyDataSetChanged()
//        notifyItemRangeInserted(prevSize + 1, elements.size)
    }

    private fun addHeaders(elements: Array<out Picture>): List<Any> {
        val groupedMap = elements.groupBy { it.request }
        val resultList = mutableListOf<Any>()
        groupedMap.forEach { entity ->
            resultList.add(entity.key).also { resultList.addAll(entity.value) }
        }
        Log.d(TAG, "addHeaders: $resultList")
        return resultList
    }


    fun removeAll() {
        data.clear()
        notifyDataSetChanged()
    }

    fun removeItems(vararg elements: Picture) {
        data.removeAll(elements)
        notifyDataSetChanged()
    }

    fun removeItems(vararg position: Int) {
        position.forEach {
            data.removeAt(it)
        }
        notifyDataSetChanged() //TODO("Rewrite")
        // TODO : 01.01.2021 delete from media
    }
}
