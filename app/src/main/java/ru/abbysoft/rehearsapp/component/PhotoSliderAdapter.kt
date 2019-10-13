package ru.abbysoft.rehearsapp.component

import android.content.Context
import android.util.Log
import com.smarteist.autoimageslider.SliderViewAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.bumptech.glide.Glide
import ru.abbysoft.rehearsapp.R
import ru.abbysoft.rehearsapp.util.imageUrl


class PhotoSliderAdapter(private val photos: ArrayList<String>, private val context: Context) :
    SliderViewAdapter<PhotoSliderAdapter.PhotoSliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): PhotoSliderViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.gallery_item, null)
        return PhotoSliderViewHolder(inflate)
    }

    override fun onBindViewHolder(viewHolder: PhotoSliderViewHolder, position: Int) {
        check(position <= photos.lastIndex + 1) { "Index out of photos array" }

        if (position == photos.size) {
            // add photo placeholder
            viewHolder.imageViewBackground.
                setImageDrawable(ContextCompat.getDrawable(context, android.R.drawable.ic_menu_add))
            viewHolder.imageViewBackground.setPadding(100)
            viewHolder.imageViewBackground.setOnClickListener { addNewPhoto(it) }
        } else {
            Glide.with(viewHolder.itemView)
                .load(imageUrl(photos[position]))
                .centerCrop()
                .into(viewHolder.imageViewBackground)
        }
    }

    private fun addNewPhoto(view: View?) {
        Log.i("ADD_IMAGE", "PHOTO ADDED")
        photos.add("placeholder")
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