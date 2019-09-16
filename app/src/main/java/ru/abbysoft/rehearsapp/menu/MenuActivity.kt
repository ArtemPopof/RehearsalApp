package ru.abbysoft.rehearsapp.menu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ru.abbysoft.rehearsapp.MapsActivity
import ru.abbysoft.rehearsapp.R
import ru.abbysoft.rehearsapp.place.PlaceViewActivity

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
    }

    fun onMapClicked(view : View) {
        MapsActivity.launchFrom(this)
    }

    fun onPlaceViewClicked(view : View) {
        PlaceViewActivity.launchForView(this, 0)
    }
}
