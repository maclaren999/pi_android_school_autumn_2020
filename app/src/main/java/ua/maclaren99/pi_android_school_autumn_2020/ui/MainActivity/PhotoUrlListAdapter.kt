package ua.maclaren99.pi_android_school_autumn_2020.ui.MainActivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.item_photo_card.view.*
import ua.maclaren99.pi_android_school_autumn_2020.R
import ua.maclaren99.pi_android_school_autumn_2020.data.network.displayWebViewActivity


class PhotoUrlListAdapter : RecyclerView.Adapter<PhotoUrlListAdapter.PhotoUrlViewHolder>() {

    private val photoItemList: MutableList<String> = mutableListOf<String>()

    class PhotoUrlViewHolder(view: View) : RecyclerView.ViewHolder(view)/*, View.OnClickListener*/ {
        val urlTextView: TextView = view.url_text_view
        val photoPreview: ImageView = view.photo_preview
        val innerLayout = view.item_photo_card_clayout
        val closeButton = view.close_button


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoUrlViewHolder =
        PhotoUrlViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_photo_card, parent, false)
        )

    override fun onBindViewHolder(holder: PhotoUrlViewHolder, position: Int) {
        holder.urlTextView.text = photoItemList[position]

//        TODO("Write one listener for all views")
        holder.closeButton.setOnClickListener {
            removeItems(position)
        }

        holder.itemView.setOnClickListener {
            displayWebViewActivity(it, it.url_text_view.text.toString())
        }
        //TODO("url generation for different image resolution")
        Glide
            .with(holder.urlTextView.context)
            .load(photoItemList[position])
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .centerCrop()
            .into(holder.photoPreview)


    }

    override fun getItemCount(): Int = photoItemList.size


    fun addItems(vararg elementsURL: String) {
        val prevSize = photoItemList.size
        photoItemList.addAll(elementsURL)
        notifyItemRangeInserted(prevSize + 1, elementsURL.size)
    }

    fun removeAll(){
        photoItemList.clear()
        notifyDataSetChanged()
    }

    fun removeItems(vararg elements: String) {
        photoItemList.removeAll(elements)
        notifyDataSetChanged()
    }

    fun removeItems(vararg positions: Int) {
        positions.forEach {
            photoItemList.removeAt(it)
        }
        notifyDataSetChanged()
    }

    class PhotoItem(){
        //TODO("")
    }

    class RequestHeaderItem(){

    }

}

