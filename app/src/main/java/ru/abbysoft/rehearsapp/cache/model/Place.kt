package ru.abbysoft.rehearsapp.cache.model

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng

@Deprecated("since model artifact")
data class Place (var name: String, var position: LatLng, var headerImage: Bitmap? = null)

