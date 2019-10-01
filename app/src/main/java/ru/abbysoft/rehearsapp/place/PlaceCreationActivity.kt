package ru.abbysoft.rehearsapp.place

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import ru.abbysoft.rehearsapp.R
import ru.abbysoft.rehearsapp.cache.CacheFactory
import ru.abbysoft.rehearsapp.databinding.ActivityPlaceCreationBinding
import ru.abbysoft.rehearsapp.model.Place
import ru.abbysoft.rehearsapp.util.MapMarkerCreator
import ru.abbysoft.rehearsapp.util.showErrorMessage
import ru.abbysoft.rehearsapp.util.validateThatNotBlank
import ru.abbysoft.rehearsapp.util.zoomMapToCurrentLocation

class PlaceCreationActivity : AppCompatActivity(), OnMapReadyCallback {

    private val TAG = PlaceCreationActivity::class.java.name

    private lateinit var markerCreator : MapMarkerCreator
    private lateinit var model : PlaceCreationViewModel

    private lateinit var nameField : EditText

    private var lastCreatedPlace : Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configureViewModel()

        nameField = findViewById(R.id.place_creation_name)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun configureViewModel() {
        val binding = DataBindingUtil.setContentView<ActivityPlaceCreationBinding>(
            this, R.layout.activity_place_creation)

        model = ViewModelProviders.of(this)[PlaceCreationViewModel::class.java]

        binding.model = model
    }

    companion object {
        fun launch(view : View) {
            view.context.startActivity(Intent(view.context, PlaceCreationActivity::class.java))
        }
    }

    override fun onMapReady(map: GoogleMap) {
        markerCreator = MapMarkerCreator(map)
        zoomMapToCurrentLocation(map)
    }

    fun save(view: View) {
        if (markerCreator.marker == null) {
            showErrorMessage(getString(R.string.place_location_not_set), this)
            return
        }
        if (!nameField.validateThatNotBlank()) return


        val location = markerCreator.marker?.position

        val place = Place(model.name, location as LatLng)

        if (lastCreatedPlace != null) {
            editExisting(CacheFactory.getDefaultCacheInstance().getPlace(lastCreatedPlace as Long),
                location)
            Log.i(TAG, "Place changed $place")
        } else {
            val id = CacheFactory.getDefaultCacheInstance().addPlace(place)
            Log.i(TAG, "Place saved $place")
            lastCreatedPlace = id
        }

        PlaceViewActivity.launchForView(this, lastCreatedPlace as Long)
    }

    private fun editExisting(place: Place, position: LatLng) {
        place.position = position
        place.name = model.name
    }
}
