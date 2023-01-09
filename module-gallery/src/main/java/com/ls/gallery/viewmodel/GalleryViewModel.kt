package com.ls.gallery.viewmodel

import androidx.lifecycle.ViewModel
import com.ls.gallery.usecase.GalleryUseCase

class GalleryViewModel : ViewModel() {

    val galleryUseCase = GalleryUseCase()
}