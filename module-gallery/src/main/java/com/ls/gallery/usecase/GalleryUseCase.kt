package com.ls.gallery.usecase

import android.content.Context
import com.ls.gallery.data.GalleryFetcherImpl
import com.ls.gallery.data.IGalleryFetcher
import com.ls.gallery.model.GalleryModel
import com.ls.gallery.model.GalleryType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class GalleryUseCase {

    private val scope = MainScope()

    private val _galleryList = MutableStateFlow<List<GalleryModel>>(emptyList())
    val galleryList: StateFlow<List<GalleryModel>> = _galleryList

    private val galleryFetcher: IGalleryFetcher = GalleryFetcherImpl()

    fun fetchGallery(context: Context) {
        scope.launch(Dispatchers.IO) {
            val list = galleryFetcher.fetchGalleryData(context, GalleryType.PHOTO)
            _galleryList.value = list
        }
    }
}