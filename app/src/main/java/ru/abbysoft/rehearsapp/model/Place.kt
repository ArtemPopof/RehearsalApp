package ru.abbysoft.rehearsapp.model

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng

data class Place (val name: String, val position: LatLng, val headerImage: Bitmap? = null)

