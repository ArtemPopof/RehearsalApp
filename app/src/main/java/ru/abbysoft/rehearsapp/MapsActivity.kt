package ru.abbysoft.rehearsapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.util.Consumer
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.activity_maps.*
import ru.abbysoft.rehearsapp.databinding.ActivityMapsBinding
import ru.abbysoft.rehearsapp.map.MapActivity
import ru.abbysoft.rehearsapp.model.Place
import ru.abbysoft.rehearsapp.model.location
import ru.abbysoft.rehearsapp.rest.ServiceFactory
import ru.abbysoft.rehearsapp.util.AsyncServiceRequest
import ru.abbysoft.rehearsapp.util.distanceFrom
import ru.abbysoft.rehearsapp.util.getShortAddressByLocation
import ru.abbysoft.rehearsapp.util.showErrorMessage

class MapsActivity : MapActivity(R.layout.activity_maps) {
    private var places: List<Place>? = null
    private val positionPlace = HashMap<LatLng, Place>()
    private lateinit var binding: ActivityMapsBinding
    private var selected: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // get all places from rest
        loadPlaces()
    }

    override fun configureLayout() {
        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_maps)
    }

    private fun loadPlaces() {
        AsyncServiceRequest<List<Place>>(
            Consumer {
                places = it
                if (mMap != null) {
                    createPlaceMarkers()
                }
            },
            Consumer {
                showErrorMessage(
                    getString(R.string.cannot_load_places),
                    this
                )
            },
            10
        ).execute(ServiceFactory.getDatabaseService().getAllPlaces())
    }

    private fun createPlaceMarkers() {
        for (place in places as List<Place>) {
            val location = place.location()
            mMap?.addMarker(createMarker(location, place.name))
            positionPlace[location] = place
        }
    }

    private fun createMarker(location: LatLng, name: String, isSelected: Boolean = false): MarkerOptions {
        val marker =  MarkerOptions().position(location).title(name)
        val iconResource = if (isSelected) BitmapDescriptorFactory.defaultMarker() else BitmapDescriptorFactory.fromResource(R.drawable.marker)
        return marker.icon(iconResource)
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
        super.onMapReady(googleMap)

        if (places != null) {
            createPlaceMarkers()
        }

        // add onclick listeners
        mMap?.setOnMarkerClickListener {
            selected?.unselect()
            it.setIcon(BitmapDescriptorFactory.defaultMarker())
            selected = it
            placeSelected(positionPlace[it.position])
            true
        }
    }

    private fun Marker.unselect() {
        this.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
    }

    private fun placeSelected(place: Place?) {
        place ?: return

        binding.currentPlace = place
        sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED

        mMap?.moveCamera(CameraUpdateFactory.newLatLng(place.location()))

        getShortAddressByLocation(place.location(), Consumer {
            binding.placeAddress = it
        })

        currentLocation ?: return
        binding.distance = currentLocation!!.distanceFrom(place.location())
    }
    // TODO position in Place model (maybe remove?)

    fun callNumber(view: View) {
        val telephone = binding.currentPlace?.telephone

        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$telephone")

        startActivity(intent)
    }

    fun openWebsite(view: View) {
        val website = binding.currentPlace?.website

        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("http:$website")

        startActivity(intent)
    }

    companion object {
        fun launchFrom(context: Context) {
            val intent = Intent(context, MapsActivity::class.java)
            context.startActivity(intent)
        }
    }
}
