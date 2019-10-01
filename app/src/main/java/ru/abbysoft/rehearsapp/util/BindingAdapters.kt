package ru.abbysoft.rehearsapp.util

import android.graphics.Bitmap
import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("app:bitmap")
    fun bitmap(view: ImageView, bitmap: Bitmap?) {
        if (bitmap == null) {
            // draw background
            return
        }
        view.setImageBitmap(bitmap)
    }

    @JvmStatic
    @BindingAdapter("app:notBlank")
    fun notEmpty(field: EditText, text: String) {
        if (text.isBlank()) {
            field.error = ("This field can't be blank")
        }

    }
}