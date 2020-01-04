package ru.abbysoft.rehearsapp.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.Visibility
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.util.Consumer
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import ru.abbysoft.rehearsapp.R
import ru.abbysoft.rehearsapp.component.RoomCardView
import ru.abbysoft.rehearsapp.model.Image
import ru.abbysoft.rehearsapp.model.Room
import ru.abbysoft.rehearsapp.model.TimeSlot

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
    @BindingAdapter("app:bitmapAsync")
    fun bitmapAsync(view: ImageView, image: Image?) {
        if (image == null) return
        view.context.loadImageDataAsync(image.name, Consumer { view.setToView(it) })
    }

    private fun ImageView.setToView(data: ByteArray) {
        Glide.with(this.context)
            .load(data)
            .centerCrop()
            .into(this)
    }

    @JvmStatic
    @BindingAdapter("app:notBlank")
    fun notEmpty(field: EditText, text: String) {
        if (text.isBlank()) {
            field.error = ("This field can't be blank")
        }

    }

    @JvmStatic
    @BindingAdapter("app:priceFrom")
    fun priceFrom(text: TextView, price: Float) {
        text.text = text.context.getString(R.string.priceFrom, price.toInt())
    }

    @JvmStatic
    @BindingAdapter("app:price")
    fun price(text: TextView, price: Float) {
        text.text = text.context.getString(R.string.dollars, price)
    }

    @JvmStatic
    @BindingAdapter("app:distance")
    fun distance(text: TextView, distance: Float) {
        if (distance >= 1.0f) {
            text.text = text.context.getString(R.string.distance, distance)
        } else {
            text.text = text.context.getString(R.string.distanceMeters, (distance * 1000).toInt())
        }
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

    @JvmStatic
    @BindingAdapter("app:disableIf")
    fun disableIf(view: View, boolean: Boolean) {
        view.isEnabled = !boolean
    }

    @JvmStatic
    @BindingAdapter("app:timeSpan")
    fun timeSpan(textView: TextView, slot: TimeSlot?) {
        if (slot == null) {
            textView.text = textView.context.getString(R.string.undefined)
            return
        }
        
        val start = getHoursAndMinutes(slot.timeStart)
        val end = getHoursAndMinutes(slot.timeEnd)
        
        textView.text = textView.context.getString(R.string.time_span, start, end)
    }
    
    private fun getHoursAndMinutes(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds - hours * 3600) / 60

        if ("$minutes".length == 1) {
            return "$hours:0$minutes"
        }
        return "$hours:$minutes"
    }
}