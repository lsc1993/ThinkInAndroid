package com.ls.gallery.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.ls.kotlin.AppInstance

fun uri2Bitmap(uri: Uri, reqWidth: Int, reqHeight: Int): Bitmap? {
    val inputStream = AppInstance.getApp().contentResolver.openInputStream(uri)
    val options = BitmapFactory.Options().apply {
        this.inJustDecodeBounds = true
    }
    BitmapFactory.decodeStream(inputStream, null, options)
    if (reqWidth > 0 && reqHeight > 0) {
        val sampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inSampleSize = sampleSize
    }
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeStream(inputStream, null, options)
}

fun calculateInSampleSize(
    options: BitmapFactory.Options,
    reqWidth: Int, reqHeight: Int
): Int {
    val width = options.outWidth
    val height = options.outHeight
    var sampleSize = 1

    if (width > reqWidth || height > reqHeight) {
        val halfWidth = width / 2
        val halfHeight = height / 2
        while ((halfWidth / sampleSize) >= reqWidth && (halfHeight / sampleSize) >= reqHeight) {
            sampleSize *= 2
        }
    }

    return sampleSize
}