package com.ls.gallery.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ls.gallery.R
import com.ls.kotlin.screenWidth

class GalleryViewHolder(itemView: View) : ViewHolder(itemView) {

    val photoIv: ImageView = itemView.findViewById(R.id.ivPhoto)

    init {
        val size = screenWidth / 3
        photoIv.layoutParams.apply {
            this.width = size
            this.height = size
        }
    }
}