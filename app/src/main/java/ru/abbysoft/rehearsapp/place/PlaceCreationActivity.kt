package ru.abbysoft.rehearsapp.place

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import ru.abbysoft.rehearsapp.R
import ru.abbysoft.rehearsapp.util.MapMarkerCreator
import ru.abbysoft.rehearsapp.util.zoomMapToCurrentLocation

class PlaceCreationActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var markerCreator : MapMarkerCreator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_creation)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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
}
