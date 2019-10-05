package ru.abbysoft.rehearsapp.place

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.core.util.Consumer
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
import ru.abbysoft.rehearsapp.model.Room
import ru.abbysoft.rehearsapp.rest.ServiceFactory
import ru.abbysoft.rehearsapp.util.*
import java.lang.Exception

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

        val place = Place()
        place.position = location.toString()
        place.name = nameField.text.toString()

        // Todo remove, only for test
        val rooms = place.rooms as ArrayList<Room>
        rooms.add(Room(-1, "Room Red", 24.5f, 2f))
        rooms.add(Room(-1, "Room VIP", 44.5f, 4.2f))
        rooms.add(Room(-1, "Room GOLD", 74.55f, 6.9f))
        //

        if (lastCreatedPlace != null) {
            //editExisting(CacheFactory.getDefaultCacheInstance().getPlace(lastCreatedPlace as Long),location)
            addPlace(place)
        } else {
            addPlace(place)
        }

    }

    private fun addPlace(place: Place) {
        Log.i(TAG, "Saving place $place")

        AsyncServiceRequest(
            Consumer<Long> { placeSaved(it) },
            Consumer { saveFailed(it) }
        ).execute(ServiceFactory.getDatabaseService().addPlace(place))
    }

    private fun placeSaved(id: Long) {
        Log.i(TAG, "Place saved with id $id")

        lastCreatedPlace = id
        PlaceViewActivity.launchForView(this, lastCreatedPlace as Long)
    }

    private fun saveFailed(ex: Exception) {
        ex.printStackTrace()
        Log.e(TAG, "Cannot save place!")
    }

    private fun editExisting(place: Place, position: LatLng) {
//        place.position = position
//        place.name = model.name
//        Log.i(TAG, "Place changed $place")
    }
}
