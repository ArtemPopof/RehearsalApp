package ru.abbysoft.rehearsapp.util

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

fun zoomMapToCurrentLocation(map : GoogleMap) {
    // Add a marker in Moscow and move the camera
    val moscow = LatLng(55.751244, 37.618423)
    //map.addMarker(MarkerOptions().position(moscow).title("Marker in Moscow"))
    map.moveCamera(CameraUpdateFactory.zoomTo(10.0f))
    map.moveCamera(CameraUpdateFactory.newLatLng(moscow))
}