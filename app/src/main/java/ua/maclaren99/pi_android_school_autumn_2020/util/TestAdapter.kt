package ua.maclaren99.pi_android_school_autumn_2020.util//import android.R
//import android.content.Context
//import android.content.Intent
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import androidx.core.content.ContextCompat
//import androidx.recyclerview.widget.RecyclerView
//import ua.maclaren99.pi_android_school_autumn_2020.ui.MainActivity
//
//private class TestAdapter(context: Context, spacePhotos: Array<SpacePhoto>) :
//    RecyclerView.Adapter<MainActivity.ImageGalleryAdapter.MyViewHolder>() {
//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): MainActivity.ImageGalleryAdapter.MyViewHolder {
//        val context: Context = parent.context
//        val inflater = LayoutInflater.from(context)
//        val photoView: View = inflater.inflate(R.layout.item_photo, parent, false)
//        return MainActivity.ImageGalleryAdapter.MyViewHolder(photoView)
//    }
//
//    override fun onBindViewHolder(
//        holder: MainActivity.ImageGalleryAdapter.MyViewHolder,
//        position: Int
//    ) {
//        val spacePhoto: SpacePhoto = mSpacePhotos[position]
//        val imageView: ImageView = holder.mPhotoImageView
//    }
//
//    override fun getItemCount(): Int {
//        return mSpacePhotos.size
//    }
//
//    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
//        View.OnClickListener {
//        var mPhotoImageView: ImageView
//        override fun onClick(view: View) {
//            val position = adapterPosition
//            if (position != RecyclerView.NO_POSITION) {
//                val spacePhoto: SpacePhoto = mSpacePhotos[position]
//                val intent = Intent(mContext, SpacePhotoActivity::class.java)
//                intent.putExtra(SpacePhotoActivity.EXTRA_SPACE_PHOTO, spacePhoto)
//                ContextCompat.startActivity(intent)
//            }
//        }
//
//        init {
//            mPhotoImageView = itemView.findViewById<View>(R.id.iv_photo) as ImageView
//            itemView.setOnClickListener(this)
//        }
//    }
//
//    private val mSpacePhotos: Array<SpacePhoto>
//    private val mContext: Context
//
//    init {
//        mContext = context
//        mSpacePhotos = spacePhotos
//    }
//}