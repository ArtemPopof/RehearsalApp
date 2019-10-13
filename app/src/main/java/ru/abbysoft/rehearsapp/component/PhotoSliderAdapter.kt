package ru.abbysoft.rehearsapp.component

import com.smarteist.autoimageslider.SliderViewAdapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import ru.abbysoft.rehearsapp.R


class PhotoSliderAdapter(private val context: Context) :
    SliderViewAdapter<PhotoSliderAdapter.PhotoSliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): PhotoSliderViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.gallery_item, null)
        return PhotoSliderViewHolder(inflate)
    }

    override fun onBindViewHolder(viewHolder: PhotoSliderViewHolder, position: Int) {
        when (position) {
            0 -> Glide.with(viewHolder.itemView)
                .load("https://images.pexels.com/photos/218983/pexels-photo-218983.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
                .centerCrop()
                .into(viewHolder.imageViewBackground)
            1 -> Glide.with(viewHolder.itemView)
                .load("https://images.pexels.com/photos/747964/pexels-photo-747964.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260")
                .centerCrop()
                .into(viewHolder.imageViewBackground)
            2 -> Glide.with(viewHolder.itemView)
                .load("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
                .centerCrop()
                .into(viewHolder.imageViewBackground)
            else -> Glide.with(viewHolder.itemView)
                .load("https://images.pexels.com/photos/218983/pexels-photo-218983.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
                .centerCrop()
                .into(viewHolder.imageViewBackground)
        }

    }

    override fun getCount(): Int {
        //slider view count could be dynamic size
        return 4
    }

    class PhotoSliderViewHolder(val itemView: View) :
        SliderViewAdapter.ViewHolder(itemView) {
        var imageViewBackground: ImageView = itemView.findViewById(R.id.gallery_item_image)
    }

}