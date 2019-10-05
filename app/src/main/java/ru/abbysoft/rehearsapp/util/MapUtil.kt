package ru.abbysoft.rehearsapp.util

import android.app.Activity
import android.content.Context
import androidx.core.util.Consumer
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

val MOSCOW = LatLng(55.751244, 37.618423)

fun zoomMapToCurrentLocation(map : GoogleMap, activity: Activity) {
    map.moveCamera(CameraUpdateFactory.zoomTo(10.0f))

    val callback = Consumer<LatLng> {
        map.moveCamera(CameraUpdateFactory.newLatLng(it))
    }

    getCurrentLocation(activity, callback)
}

fun getCurrentLocation(context: Context, callback: Consumer<LatLng>) {
    val client = LocationServices.getFusedLocationProviderClient(context)
    client.lastLocation.addOnSuccessListener {
        if (it == null) callback.accept(MOSCOW)
        else callback.accept(LatLng(it.latitude, it.longitude))
    }.addOnFailureListener {
        it.printStackTrace()
        callback.accept(MOSCOW)
    }
}

