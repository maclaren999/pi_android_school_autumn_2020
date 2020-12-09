package ua.maclaren99.pi_android_school_autumn_2020.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.item_photo_card.view.*
import ua.maclaren99.pi_android_school_autumn_2020.R
import ua.maclaren99.pi_android_school_autumn_2020.data.network.displayWebViewActivity


class PhotoUrlListAdapter : RecyclerView.Adapter<PhotoUrlListAdapter.PhotoUrlViewHolder>() {

    private val photoItemList: MutableList<String> = mutableListOf<String>()

    class PhotoUrlViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val urlTextView: TextView = view.url_text_view
        val photoPreview: ImageView = view.photo_preview

        override fun onClick(v: View?) {
            Log.d(TAG, "onClick")
            //TODO("Do not get url from View")
            v?.let {
                displayWebViewActivity(it, it.url_text_view.text.toString())
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoUrlViewHolder =
        PhotoUrlViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_photo_card, parent, false)
        )

    override fun onBindViewHolder(holder: PhotoUrlViewHolder, position: Int) {
        holder.urlTextView.text = photoItemList[position]

        //TODO("url generation for different image resolution")
        Glide
            .with(holder.urlTextView.context)
            .load(photoItemList[position])
            .diskCacheStrategy(DiskCacheStrategy.RESULT)
            .centerCrop()
            .into(holder.photoPreview)

    }

    override fun getItemCount(): Int = photoItemList.size

    fun addItems(vararg elements: String) {
        val prevSize = photoItemList.size
        photoItemList.addAll(elements)
        notifyItemRangeInserted(prevSize + 1, elements.size)
    }

    fun removeItems(vararg elements: String) {
        photoItemList.removeAll(elements)
    }
}

