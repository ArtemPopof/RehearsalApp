package ru.abbysoft.rehearsapp.util;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

public class BindingAdapters {

    @BindingAdapter("app:bitmap")
    public static void bitmap(ImageView view, Bitmap bitmap) {
        view.setImageBitmap(bitmap);
    }
}
