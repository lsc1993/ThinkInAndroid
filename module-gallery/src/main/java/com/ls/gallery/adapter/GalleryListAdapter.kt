package com.ls.gallery.adapter

import android.os.Build
import android.util.Size
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.ls.gallery.R
import com.ls.gallery.model.GalleryModel
import com.ls.gallery.util.Logger
import com.ls.kotlin.AppInstance
import com.ls.kotlin.screenWidth

class GalleryListAdapter : Adapter<ViewHolder>() {

    private val width = screenWidth / 3
    private val height = screenWidth / 3

    private val dataList: ArrayList<GalleryModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.gallery_item_view_layout, parent, false)
        return GalleryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is GalleryViewHolder) {
            val data = dataList[position]
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                holder.photoIv.setImageBitmap(
                    AppInstance.getApp().contentResolver.loadThumbnail(
                        data.uri,
                        Size(width, height),
                        null
                    )
                )
                Logger.d("GalleryListAdapter", "load from uri position: $position")
            } else {
                Glide.with(holder.itemView.context)
                    .asBitmap()
                    .load(data.path)
                    .into(holder.photoIv)
                Logger.d("GalleryListAdapter", "load from glide position: $position")
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(list: List<GalleryModel>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

}