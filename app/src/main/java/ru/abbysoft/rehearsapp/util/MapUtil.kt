package ru.abbysoft.rehearsapp.util

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

fun zoomMapToCurrentLocation(map : GoogleMap) {
    map.moveCamera(CameraUpdateFactory.zoomTo(10.0f))
    map.moveCamera(CameraUpdateFactory.newLatLng(getCurrentLocation()))
}

fun getCurrentLocation() : LatLng {
    // TODO get device last known location
    // Moscow location returned
    return LatLng(55.751244, 37.618423)
}