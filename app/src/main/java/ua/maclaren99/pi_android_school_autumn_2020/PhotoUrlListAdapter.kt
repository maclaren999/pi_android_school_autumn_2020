package ua.maclaren99.pi_android_school_autumn_2020

import android.R
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_photo_card.view.*



class PhotoUrlListAdapter : RecyclerView.Adapter<PhotoUrlListAdapter.PhotoUrlViewHolder>() {

    val photoItemList: MutableList<String> = mutableListOf<String>()

    class PhotoUrlViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val urlTextView: TextView = view.url_text_view
        val photoPreview: ImageView = view.photo_preview
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoUrlViewHolder =
        PhotoUrlViewHolder(
            LayoutInflater.from(parent.context).inflate(, parent, false)
        )

    override fun onBindViewHolder(holder: PhotoUrlViewHolder, position: Int) {
        holder.urlTextView.text = photoItemList[position].toString()
        holder.photoPreview

    }

    override fun getItemCount(): Int = photoItemList.size

    fun addToItemList(vararg url : String){
        val prevSize = photoItemList.size
        photoItemList.addAll(url)
        notifyItemRangeInserted(prevSize+1, url.size)
    }
}

// ...

