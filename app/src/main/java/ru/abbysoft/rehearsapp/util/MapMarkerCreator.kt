package ru.abbysoft.rehearsapp.util

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

// This class allows user to click on map and create marker
// If one marker mode is used, then marker will be moved when
// user clicks another time
class MapMarkerCreator(val map: GoogleMap, val onlyOne: Boolean = true) {

    var marker : Marker? = null

    init {
        map.setOnMapClickListener {
            if (marker == null) {
                marker = createMarker(it)
            } else {
                moveMarker(it)
            }
        }
    }

    private fun createMarker(position : LatLng) : Marker {
        return map.addMarker(MarkerOptions().position(position))
    }

    private fun moveMarker(position: LatLng) {
        marker?.position = position
    }

}