package ru.abbysoft.rehearsapp.menu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.util.Consumer
import ru.abbysoft.rehearsapp.MapsActivity
import ru.abbysoft.rehearsapp.R
import ru.abbysoft.rehearsapp.model.Place
import ru.abbysoft.rehearsapp.place.PlaceCreationActivity
import ru.abbysoft.rehearsapp.place.PlaceViewActivity
import ru.abbysoft.rehearsapp.rest.ServiceFactory
import ru.abbysoft.rehearsapp.util.AsyncServiceRequest
import ru.abbysoft.rehearsapp.util.showErrorMessage
import java.util.*

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
    }

    fun onMapClicked(view : View) {
        MapsActivity.launchFrom(this)
    }

    fun onPlaceViewClicked(view : View) {
        AsyncServiceRequest<List<Place>>(
            Consumer {
                PlaceViewActivity.launchForView(this, getRandomId(it))
            },
            Consumer {
                showErrorMessage("Cannot open random place", this)
            }
        ).execute(ServiceFactory.getDatabaseService().getAllPlaces())
    }

    private fun getRandomId(places: List<Place>) : Long {
        val rand = Random(System.currentTimeMillis())

        val randIndex = rand.nextInt(places.size)
        return places[randIndex].id
    }

    fun onAddNewPlaceClicked(view : View) {
        PlaceCreationActivity.launch(view)
    }
}
