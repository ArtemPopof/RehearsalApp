package ru.abbysoft.rehearsapp.util

import android.graphics.Bitmap
import kotlin.math.floor

fun cropImage(bitmap: Bitmap, width: Int, height: Int) : Bitmap {
    return Bitmap.createBitmap(bitmap, 0, 0, width, height)
}

fun shrinkWithProportion(bitmap: Bitmap, height: Int = 0, width: Int = 0) : Bitmap {
    if (height == 0 && width == 0) {
        return bitmap
    }

    val coeff : Float
    if (height == 0) {
        // shrink to width
        coeff = width * 1.0f / bitmap.width
    } else {
        // shrink to height
        coeff = height * 1.0f / bitmap.height
    }

    return Bitmap.createScaledBitmap(bitmap,
        (bitmap.width * coeff).toInt(), (bitmap.height * coeff).toInt(), true)
}