package ru.abbysoft.rehearsapp.model

import com.google.android.gms.maps.model.LatLng
import ru.abbysoft.rehearsapp.util.MOSCOW

fun Place.location() : LatLng {
    if (position.isBlank()) {
        return MOSCOW
    }

    val cleared = position.substring(10, position.lastIndex)
    val split = cleared.split(",")
    return LatLng(split[0].toDouble(), split[1].toDouble())
}