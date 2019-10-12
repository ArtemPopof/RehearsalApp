package ru.abbysoft.rehearsapp.util

import android.graphics.Bitmap
import android.opengl.Visibility
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import ru.abbysoft.rehearsapp.R
import ru.abbysoft.rehearsapp.component.RoomCardView
import ru.abbysoft.rehearsapp.model.Room

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

    @JvmStatic
    @BindingAdapter("app:price")
    fun price(text: TextView, price: Float) {
        text.text = text.context.getString(R.string.priceFrom, price)
    }

    @JvmStatic
    @BindingAdapter("app:area")
    fun area(text: TextView, area: Float) {
        text.text = text.context.getString(R.string.areaM2, area)
    }

    @JvmStatic
    @BindingAdapter("app:hide")
    fun hide(view: View, boolean: Boolean) {
        if (boolean) {
            view.visibility = View.GONE
        } else {
            view.visibility = View.VISIBLE
        }
    }
}