package com.ls.gallery.data

import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import com.ls.gallery.model.GalleryModel
import com.ls.gallery.model.GalleryType
import com.ls.gallery.util.Logger

class GalleryFetcherImpl : IGalleryFetcher {

    override suspend fun fetchGalleryData(
        context: Context,
        galleryType: GalleryType
    ): List<GalleryModel> {
        val dataList = ArrayList<GalleryModel>()
        val uri = when (galleryType) {
            GalleryType.PHOTO -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    MediaStore.Images.Media.getContentUri(
                        MediaStore.VOLUME_EXTERNAL
                    )
                } else {
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }
            }
            GalleryType.VIDEO -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    MediaStore.Video.Media.getContentUri(
                        MediaStore.VOLUME_EXTERNAL
                    )
                } else {
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                }
            }
            else -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    MediaStore.Images.Media.getContentUri(
                        MediaStore.VOLUME_EXTERNAL
                    )
                } else {
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }
            }
        }
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_MODIFIED
        )
        Logger.d("GalleryFetcherImpl", "start query image......")
        val cursor = context.contentResolver.query(
            uri,
            projection, null, null, null
        )
        Logger.d("GalleryFetcherImpl", "get cursor......")
        cursor?.let {
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            val nameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val imageUri = ContentUris.withAppendedId(uri, id)
                val name = it.getString(nameColumn)
                val path = it.getString(pathColumn)
                //Logger.d("GalleryFetcherImpl", "name: $name")
                Logger.d("GalleryFetcherImpl", "imageUri: $imageUri")
                val galleryModel = GalleryModel(imageUri, path, name)
                dataList.add(galleryModel)
            }

            it.close()
            Logger.d("GalleryFetcherImpl", "end query image......")
        }

        return dataList
    }
}