package ua.maclaren99.pi_android_school_autumn_2020.ui.FavoritesActivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.item_photo_card.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.maclaren99.pi_android_school_autumn_2020.R
import ua.maclaren99.pi_android_school_autumn_2020.data.database.Picture
import ua.maclaren99.pi_android_school_autumn_2020.data.network.displayWebViewActivity
import ua.maclaren99.pi_android_school_autumn_2020.util.appDatabase


class FavoritesListAdapter : RecyclerView.Adapter<FavoritesListAdapter.FavoritePhotoItemHolder>() {

    private val roomPicturesList: MutableList<Picture> = mutableListOf<Picture>()

    class FavoritePhotoItemHolder(view: View) :
        RecyclerView.ViewHolder(view)/*, View.OnClickListener*/ {
        val urlTextView: TextView = view.url_text_view
        val photoPreview: ImageView = view.photo_preview
        val innerLayout = view.item_photo_card_clayout
        val closeButton = view.close_button


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritePhotoItemHolder =
        FavoritePhotoItemHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_photo_card, parent, false)
        )

    override fun onBindViewHolder(holder: FavoritePhotoItemHolder, position: Int) {
        holder.urlTextView.text = roomPicturesList[position].url

        holder.closeButton.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    appDatabase.pictureDAO().delete(roomPicturesList[position])
                }
                removeItems(position)
            }
        }

        holder.itemView.setOnClickListener {
            displayWebViewActivity(it, it.url_text_view.text.toString())
        }

        Glide
            .with(holder.urlTextView.context)
            .load(roomPicturesList[position].uri)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .centerCrop()
            .into(holder.photoPreview)

    }

    override fun getItemCount(): Int = roomPicturesList.size

    fun addItems(vararg elements: Picture) {
        val prevSize = roomPicturesList.size
        roomPicturesList.addAll(elements)
        notifyItemRangeInserted(prevSize + 1, elements.size)
    }

    fun removeAll() {
        roomPicturesList.clear()
        notifyDataSetChanged()
    }

    fun removeItems(vararg elements: Picture) {
        roomPicturesList.removeAll(elements)
        notifyDataSetChanged()
    }

    fun removeItems(vararg elements: Int) {
        elements.forEach {
            roomPicturesList.removeAt(it)
        }
        notifyDataSetChanged() //TODO("Rewrite")
    }
}
