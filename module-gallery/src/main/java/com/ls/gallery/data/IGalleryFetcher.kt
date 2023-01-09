package com.ls.gallery.data

import android.content.Context
import com.ls.gallery.model.GalleryModel
import com.ls.gallery.model.GalleryType

interface IGalleryFetcher {
    suspend fun fetchGalleryData(
        context: Context,
        galleryType: GalleryType
    ) : List<GalleryModel>
}