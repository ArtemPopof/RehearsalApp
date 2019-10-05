package ru.abbysoft.rehearsapp

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Consumer

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import ru.abbysoft.rehearsapp.model.Place
import ru.abbysoft.rehearsapp.model.location
import ru.abbysoft.rehearsapp.rest.ServiceFactory
import ru.abbysoft.rehearsapp.util.AsyncServiceRequest
import ru.abbysoft.rehearsapp.util.showErrorMessage
import ru.abbysoft.rehearsapp.util.zoomMapToCurrentLocation

private const val MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private var places: List<Place>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkForPermissionAndGrantIfNeeded(this)

        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // get all places from rest
        AsyncServiceRequest<List<Place>>(
            Consumer {
                places = it
                if (mMap != null) {
                    createPlaceMarkers()
                }
            },
            Consumer {
                showErrorMessage(getString(R.string.cannot_load_places),
                    this)
            },
            10
        ).execute(ServiceFactory.getDatabaseService().getAllPlaces())
    }

    private fun checkForPermissionAndGrantIfNeeded(activity: Activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            grantLocationPermission(activity)
        }
    }

    private fun grantLocationPermission(activity: Activity) {
        ActivityCompat.requestPermissions(activity,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    if (mMap == null) return
                    zoomMapToCurrentLocation(mMap as GoogleMap, this)
                }
                return
            }
            else -> {
            }
        }
    }

    private fun createPlaceMarkers() {
        for (place in places as List<Place>) {
            mMap?.addMarker(MarkerOptions().position(place.location()).title(place.name))
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        zoomMapToCurrentLocation(googleMap, this)

        if (places != null) {
            createPlaceMarkers()
        }
    }

    companion object {
        fun launchFrom(context: Context) {
            val intent = Intent(context, MapsActivity::class.java)
            context.startActivity(intent)
        }
    }
}
