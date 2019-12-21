package ru.abbysoft.rehearsapp.map

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import ru.abbysoft.rehearsapp.R
import ru.abbysoft.rehearsapp.util.MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION
import ru.abbysoft.rehearsapp.util.checkForPermissionAndGrantIfNeeded
import ru.abbysoft.rehearsapp.util.zoomMapToCurrentLocation

open class MapActivity(private val layoutId: Int) : AppCompatActivity(), OnMapReadyCallback {

    protected var mMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        configureLayout()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    protected open fun configureLayout() {
        setContentView(layoutId)
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

        checkForPermissionAndGrantIfNeeded()

        zoomMapToCurrentLocation(mMap as GoogleMap, this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
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
}