package ru.abbysoft.rehearsapp.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Consumer
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import ru.abbysoft.rehearsapp.R
import ru.abbysoft.rehearsapp.map.AddressResultReceiver
import ru.abbysoft.rehearsapp.map.FetchAddressIntentService
import ru.abbysoft.rehearsapp.map.MapActivity

val MOSCOW = LatLng(55.751244, 37.618423)
const val MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1

fun zoomMapToCurrentLocation(map : GoogleMap, activity: Activity) {
    map.moveCamera(CameraUpdateFactory.zoomTo(10.0f))

    activity.getAddressByLocation(
        MOSCOW,
        Consumer {
                address -> Toast.makeText(activity, "Address is $address", Toast.LENGTH_LONG).show()
        })

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

fun Context.getAddressByLocation(location: LatLng, consumer: Consumer<String>) {
    if (!Geocoder.isPresent()) {
        Toast.makeText(this,
            R.string.no_geocoder_available,
            Toast.LENGTH_LONG).show()
        return
    }

    val intent = Intent(this, FetchAddressIntentService::class.java).apply {
        putExtra(FetchAddressIntentService.Constants.RECEIVER, AddressResultReceiver(consumer))
        putExtra(FetchAddressIntentService.Constants.LOCATION_DATA_EXTRA, location)
    }
    startService(intent)
}

fun Activity.checkForPermissionAndGrantIfNeeded() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
        grantLocationPermission(this)
    }
}

private fun grantLocationPermission(activity: Activity) {
    ActivityCompat.requestPermissions(activity,
        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION
    )
}

