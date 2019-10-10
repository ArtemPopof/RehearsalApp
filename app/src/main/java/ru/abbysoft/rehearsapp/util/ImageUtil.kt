package ru.abbysoft.rehearsapp.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.util.Consumer
import ru.abbysoft.rehearsapp.R
import ru.abbysoft.rehearsapp.model.Place
import ru.abbysoft.rehearsapp.rest.ServiceFactory
import java.io.ByteArrayOutputStream

private const val TAG = "ImageUtils"

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

fun Bitmap.toByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()

    compress(Bitmap.CompressFormat.WEBP, 100, stream)
    return stream.toByteArray()
}

fun loadBitmapOrNull(place: Place, bitmapConsumer: Consumer<Bitmap>, context: Context) {
    val call = ServiceFactory.getImageService().getImage(longIdToString(place.headerImageId))

    AsyncServiceRequest(
        Consumer<ByteArray> {
            bitmapConsumer.accept(BitmapFactory.decodeByteArray(it, 0, it.size))
        },
        Consumer {
            Log.e(TAG, it.toString())
            it.printStackTrace()
            showErrorMessage(context.getString(R.string.cannot_retrieve_data), context)
        }
    ).execute(call)
}