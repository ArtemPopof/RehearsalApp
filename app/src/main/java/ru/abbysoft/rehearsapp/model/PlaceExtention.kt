package ru.abbysoft.rehearsapp.model

import com.google.android.gms.maps.model.LatLng

fun Place.location() : LatLng {
    val cleared = position.substring(10, position.lastIndex)
    val split = cleared.split(",")
    return LatLng(split[0].toDouble(), split[1].toDouble())
}