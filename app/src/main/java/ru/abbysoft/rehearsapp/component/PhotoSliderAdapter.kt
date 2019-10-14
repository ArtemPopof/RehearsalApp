package ru.abbysoft.rehearsapp.component

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import com.smarteist.autoimageslider.SliderViewAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.util.Consumer
import androidx.core.view.setPadding
import com.bumptech.glide.Glide
import ru.abbysoft.rehearsapp.R
import ru.abbysoft.rehearsapp.model.Image
import ru.abbysoft.rehearsapp.util.loadImageDataAsync
import ru.abbysoft.rehearsapp.util.pickImage


class PhotoSliderAdapter(private val photos: ArrayList<Image>, private val activity: Activity) :
    SliderViewAdapter<PhotoSliderAdapter.PhotoSliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): PhotoSliderViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.gallery_item, null)
        return PhotoSliderViewHolder(inflate)
    }

    override fun onBindViewHolder(viewHolder: PhotoSliderViewHolder, position: Int) {
        check(position <= photos.lastIndex + 1) { "Index out of photos array" }

        if (position == 0) {
            // add photo placeholder
            viewHolder.imageViewBackground.
                setImageDrawable(ContextCompat.getDrawable(activity, android.R.drawable.ic_menu_add))
            viewHolder.imageViewBackground.setPadding(100)
            viewHolder.imageViewBackground.setOnClickListener { addNewPhoto(it) }
        } else {
            val data = activity.loadImageDataAsync(photos[position - 1].name, Consumer { testLOad(it, viewHolder) })
        }
    }

    private fun testLOad(it: ByteArray, viewHolder: PhotoSliderViewHolder) {
//        val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
        Glide.with(viewHolder.itemView)
            .load(it)
            .centerCrop()
            .into(viewHolder.imageViewBackground)
    }

    private fun addNewPhoto(view: View?) {
        pickImage(activity)
    }

    fun onPhotoAdded(url: String) {
        Log.i("SLIDER_ADAPTER", "photo added $url")
        photos.add(Image(name = url))
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return photos.size + 1
    }

    class PhotoSliderViewHolder(val itemView: View) :
        SliderViewAdapter.ViewHolder(itemView) {
        var imageViewBackground: ImageView = itemView.findViewById(R.id.gallery_item_image)
    }

}